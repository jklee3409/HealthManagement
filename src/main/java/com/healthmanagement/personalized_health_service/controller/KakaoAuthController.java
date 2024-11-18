package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/oauth/kakao")
@CrossOrigin(origins = "http://localhost:3000")
public class KakaoAuthController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthController.class);

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.api.token.url}")
    private String kakaoTokenUrl;

    @Value("${kakao.api.userinfo.url}")
    private String kakaoUserInfoUrl;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public KakaoAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> data) {
        logger.info("Kakao Login Request Received - Redirect URI: {}", redirectUri);

        String code = data.get("code");

        if (code == null || code.isEmpty()) {
            logger.error("Authorization code is missing");
            return ResponseEntity.badRequest().body("Authorization code is required");
        }

        try {
            // Detailed logging for each step
            logger.info("Attempting to get Kakao Access Token");
            String accessToken = getKakaoAccessToken(code);
            logger.info("Successfully obtained access token: {}",
                    accessToken != null ? "Token received" : "Token is null");

            logger.info("Attempting to get Kakao User Info");
            Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
            logger.info("Successfully retrieved user info");

            // Debug print of user info
            logger.info("User Info: {}", userInfo);

            return processKakaoUser(userInfo);

        } catch (RestClientException e) {
            logger.error("Rest Client Exception during Kakao login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Kakao login: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during Kakao login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    private String getKakaoAccessToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoApiKey);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            logger.info("Token Request Params: {}", params);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    kakaoTokenUrl,
                    request,
                    Map.class
            );

            logger.info("Token Response Status: {}", response.getStatusCode());
            logger.info("Token Response Body: {}", response.getBody());

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                logger.error("Failed to obtain access token");
                throw new RuntimeException("Failed to obtain access token");
            }

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            logger.error("Error getting Kakao Access Token", e);
            throw e;
        }
    }

    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    kakaoUserInfoUrl,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            logger.info("User Info Response Status: {}", response.getStatusCode());
            logger.info("User Info Response Body: {}", response.getBody());

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                logger.error("Failed to obtain user info");
                throw new RuntimeException("Failed to obtain user info");
            }

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error getting Kakao User Info", e);
            throw e;
        }
    }

    private ResponseEntity<?> processKakaoUser(Map<String, Object> userInfo) {
        try {
            // Safely extract user information with null checks
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            if (kakaoAccount == null) {
                logger.error("Kakao Account is null in user info");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid user information");
            }

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile == null) {
                logger.error("Profile is null in Kakao Account");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid user profile");
            }

            String kakaoId = userInfo.get("id").toString();
            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");

            logger.info("Processing Kakao User - Email: {}, Nickname: {}", email, nickname);

            // Check for existing user
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                logger.info("Existing user found: {}", existingUser.get().getEmail());
                return ResponseEntity.ok(existingUser.get());
            }

            // Create new user
            User newUser = new User();
            newUser.setUsername(nickname);
            newUser.setEmail(email);
            newUser.setPassword(""); // Empty password for OAuth users
            newUser.setAge(0);
            newUser.setWeight(0.0);
            newUser.setHeight(0.0);
            newUser.setGender("");
            newUser.setSkeletalMuscleMass(0.0);
            newUser.setHealthGoal("건강 개선"); // Default health goal

            User savedUser = userRepository.save(newUser);
            logger.info("New user created: {}", savedUser.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            logger.error("Error processing Kakao user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing user: " + e.getMessage());
        }
    }
}