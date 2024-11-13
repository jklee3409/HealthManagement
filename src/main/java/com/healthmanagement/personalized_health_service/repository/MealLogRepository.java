package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findTop5ByUserOrderByDateDesc(User user);
    List<MealLog> findByUserId(Long userId);
}
