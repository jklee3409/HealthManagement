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

    public Recommendation generateRecommendation(Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);

        User user = optionalUser.orElseThrow(() ->
                new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.")
        );

        // 기본 추천 생성 로직
        String dietRecommendation = "고단백 저칼로리 식단 추천";
        String exerciseRecommendation = "유산소 운동 30분";

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setDietRecommendation(dietRecommendation);
        recommendation.setExerciseRecommendation(exerciseRecommendation);

        return recommendationRepository.save(recommendation);
    }

    public Recommendation getRecommendation(Long userId) {
        return recommendationRepository.findByUserId(userId);
    }
}
