package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.dto.MealExerciseResponse;
import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.service.LogService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diet-exercise")
public class MealExerciseController {

    @Autowired
    private LogService logService;

    @GetMapping
    public MealExerciseResponse getMealAndExerciseData(@RequestParam Long userId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<MealLog> mealLogs = logService.getMealLogsByDate(userId, localDate);
        List<ExerciseLog> exerciseLogs = logService.getExerciseLogsByDate(userId, localDate);

        return new MealExerciseResponse(mealLogs, exerciseLogs);
    }
}
