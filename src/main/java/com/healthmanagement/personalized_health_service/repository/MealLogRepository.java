package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.MealLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserId(Long userId);
}
