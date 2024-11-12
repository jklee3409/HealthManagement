package com.healthmanagement.personalized_health_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "exercise_logs")
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String exerciseName;

    @Column(nullable = false)
    private Double exerciseTime; // 운동 시간 (분)

    @Column(nullable = false)
    private Double caloriesBurned;

    @Column
    private LocalDate date;

    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }
}
