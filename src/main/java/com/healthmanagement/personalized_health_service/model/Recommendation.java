package com.healthmanagement.personalized_health_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String dietRecommendation;

    @Column
    private String exerciseRecommendation;

    public void setUser(User user) {
        this.user = user;
    }

    public void setDietRecommendation(String dietRecommendation) {
        this.dietRecommendation = dietRecommendation;
    }

    public void setExerciseRecommendation(String exerciseRecommendation) {
        this.exerciseRecommendation = exerciseRecommendation;
    }
}
