package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.model.Recommendation;
import com.healthmanagement.personalized_health_service.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/{userId}")
    public ResponseEntity<Recommendation> generateRecommendation(@PathVariable Long userId) {
        Recommendation recommendation = recommendationService.generateRecommendation(userId);
        return new ResponseEntity<>(recommendation, HttpStatus.CREATED);
    }

    @GetMapping("/{userID}")
    public ResponseEntity<Recommendation> getRecommendation(@PathVariable Long userID) {
        Recommendation recommendation = recommendationService.getRecommendation(userID);
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }
}
