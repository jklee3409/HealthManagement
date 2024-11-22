package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.dto.LoginRequest;
import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.UserRepository;
import com.healthmanagement.personalized_health_service.service.UserService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://fitwell-healthcare.netlify.app")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/register/kakao")
    public ResponseEntity<User> registerKakaoUser(@RequestBody User user) {
        User kakaoUser = userService.findUserByEmail(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        kakaoUser.setAge(user.getAge());
        kakaoUser.setGender(user.getGender());
        kakaoUser.setHeight(user.getHeight());
        kakaoUser.setWeight(user.getWeight());
        kakaoUser.setSkeletalMuscleMass(user.getSkeletalMuscleMass());

        userRepository.save(kakaoUser);

        return new ResponseEntity<>(kakaoUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/updatesMetrics")
    public ResponseEntity<String> updatesMetrics(
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, Object> updates) {

        try {
            if (updates.containsKey("weight")) {
                updates.put("weight", parseToDouble(updates.get("weight")));
            }
            if (updates.containsKey("skeletalMuscleMass")) {
                updates.put("skeletalMuscleMass", parseToDouble(updates.get("skeletalMuscleMass")));
            }

            userService.updateUserMetrics(userId, updates);
            return ResponseEntity.ok("Successfully updated user metrics");

        } catch (Exception e) {
            String errorMessage = String.format("Failed to update user metrics for userId %d: %s. Data: %s",
                    userId, e.getMessage(), updates);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private Double parseToDouble(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value format: " + value);
            }
        }

        throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getSimpleName());
    }
}
