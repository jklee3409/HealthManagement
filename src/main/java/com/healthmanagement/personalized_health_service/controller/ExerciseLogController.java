package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.ExerciseLogRepository;
import com.healthmanagement.personalized_health_service.repository.UserRepository;
import com.healthmanagement.personalized_health_service.service.CalorieCalculatorService;
import com.healthmanagement.personalized_health_service.service.LogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseLogController {

    private final ExerciseLogRepository exerciseLogRepository;
    private final UserRepository userRepository;

    @Autowired
    private CalorieCalculatorService calorieCalculatorService;

    @Autowired
    public ExerciseLogController(ExerciseLogRepository exerciseLogRepository, UserRepository userRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ExerciseLog> create(@RequestBody ExerciseLog exerciseLog, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        double caloriesBurned = calorieCalculatorService.calculateCalories(
                exerciseLog.getExerciseName(), exerciseLog.getExerciseTime(), user.getWeight());

        exerciseLog.setCaloriesBurned(caloriesBurned);
        exerciseLog.setUser(user);
        ExerciseLog savedExerciseLog = exerciseLogRepository.save(exerciseLog);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedExerciseLog);
    }
}
