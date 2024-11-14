package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findTop5ByUserOrderByDateDesc(User user);
    List<ExerciseLog> findByUserId(Long userId);
    List<ExerciseLog> findByUserIdAndDate(Long userId, LocalDate date);
    @Query("SELECT e FROM ExerciseLog e WHERE e.user.id = :userId AND e.date >= :startDate")
    List<ExerciseLog> findExerciseLogByUserIdAndDateAfter(Long userId, LocalDate startDate);
}
