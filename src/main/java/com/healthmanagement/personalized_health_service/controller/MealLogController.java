package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.MealLogRepository;
import com.healthmanagement.personalized_health_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diet")
public class MealLogController {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;

    @Autowired
    public MealLogController(MealLogRepository mealLogRepository, UserRepository userRepository) {
        this.mealLogRepository = mealLogRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<MealLog> addMealLog(@RequestBody MealLog mealLog, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        mealLog.setUser(user);
        MealLog savedMealLog = mealLogRepository.save(mealLog);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMealLog);
    }
}
