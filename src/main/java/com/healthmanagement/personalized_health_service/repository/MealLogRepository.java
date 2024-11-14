package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.model.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findTop5ByUserOrderByDateDesc(User user);
    List<MealLog> findByUserId(Long userId);
    List<MealLog> findByUserIdAndDate(Long userId, LocalDate date);
    @Query("SELECT m FROM MealLog m WHERE m.user.id = :userId AND m.date >= :startDate")
    List<MealLog> findMealLogsByUserIdAndDateAfter(Long userId, LocalDate startDate);
}
