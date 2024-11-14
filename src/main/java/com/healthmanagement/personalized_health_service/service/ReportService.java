package com.healthmanagement.personalized_health_service.service;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.repository.ExerciseLogRepository;
import com.healthmanagement.personalized_health_service.repository.MealLogRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private MealLogRepository mealLogRepository;

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    public Map<String, Object> getWeeklyReport(Long userId) {
        return generateReport(userId, LocalDate.now().minusDays(7));
    }

    public Map<String, Object> getMonthlyReport(Long userId) {
        return generateReport(userId, LocalDate.now().minusDays(30));
    }

    private Map<String, Object> generateReport(Long userId, LocalDate startDate) {
        List<MealLog> mealLogs = mealLogRepository.findMealLogsByUserIdAndDateAfter(userId, startDate);
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findExerciseLogByUserIdAndDateAfter(userId, startDate);

        Map<String, Double> caloriesMap = new HashMap<>();
        Map<String, Double> exerciseMap = new HashMap<>();

        for(MealLog mealLog : mealLogs) {
            String date = mealLog.getDate().toString();
            caloriesMap.put(date, caloriesMap.getOrDefault(date, 0.0) + mealLog.getCalories());
        }

        for(ExerciseLog exerciseLog : exerciseLogs) {
            String date = exerciseLog.getDate().toString();
            exerciseMap.put(date, exerciseMap.getOrDefault(date, 0.0) + exerciseLog.getCaloriesBurned());
        }

        Map<String, Object> report = new HashMap<>();
        report.put("dates", caloriesMap.keySet());
        report.put("calories", caloriesMap.values());
        report.put("exercise", exerciseMap.values());

        return report;
    }
}
