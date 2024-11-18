package com.healthmanagement.personalized_health_service.repository;

import com.healthmanagement.personalized_health_service.model.UserFeedback;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
    List<UserFeedback> findByUserIdAndTimestampBetween(Long id, LocalDateTime start, LocalDateTime end);
}
