package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findTop5ByUserOrderByDateDesc(User user);
    List<ExerciseLog> findByUserId(Long userId);
}
