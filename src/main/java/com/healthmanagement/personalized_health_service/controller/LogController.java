package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.model.MealLog;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/meal")
    public ResponseEntity<MealLog> addMealLog(@RequestBody MealLog mealLog) {
        MealLog createdMealLog = logService.addMealLog(mealLog);
        return new ResponseEntity<>(createdMealLog, HttpStatus.CREATED);
    }

    @GetMapping("/meal/{userId}")
    public ResponseEntity<List<MealLog>> getMealLogsByUserId(@PathVariable Long userId) {
        List<MealLog> mealLogs = logService.getMealLogsByUserId(userId);
        return new ResponseEntity<>(mealLogs, HttpStatus.OK);
    }
}
