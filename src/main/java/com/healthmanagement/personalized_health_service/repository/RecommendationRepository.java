package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    Recommendation findByUserId(Long userId);
}



