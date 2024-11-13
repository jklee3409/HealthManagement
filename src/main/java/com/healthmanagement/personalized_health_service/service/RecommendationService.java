package com.healthmanagement.personalized_health_service.service;

import com.healthmanagement.personalized_health_service.model.Recommendation;
import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.RecommendationRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatGptApiClient chatGptApiClient;

    public Recommendation generateRecommendation(Long userId) {
        Optional<User> optionalUser = userService.findUserById(userId);

        User user = optionalUser.orElseThrow(() ->
                new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.")
        );

        String prompt = "사용자의 목표가 " + user.getGoal() + "일 때, 추천할 식단과 운동을 알려주세요.";
        String recommendationText = chatGptApiClient.getRecommendation(prompt);

        String dietRecommendation = extractDietRecommendation(recommendationText);
        String exerciseRecommendation = extractExerciseRecommendation(recommendationText);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setDietRecommendation(dietRecommendation);
        recommendation.setExerciseRecommendation(exerciseRecommendation);

        return recommendationRepository.save(recommendation);
    }

    public Recommendation getRecommendation(Long userId) {
        return recommendationRepository.findByUserId(userId);
    }

    private String extractDietRecommendation(String recommendationText) {
        return recommendationText.split("운동 추천:")[0].replace("식단 추천:", "").trim();
    }

    private String extractExerciseRecommendation(String recommendationText) {
        String[] parts = recommendationText.split("운동 추천:");
        return parts.length > 1 ? parts[1].trim() : "";
    }
}
