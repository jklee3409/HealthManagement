package com.healthmanagement.personalized_health_service.dto;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.MealLog;
import java.util.List;

public class MealExerciseResponse {
    private List<MealLog> mealLogs;
    private List<ExerciseLog> exerciseLogs;

    public MealExerciseResponse(List<MealLog> mealLogs, List<ExerciseLog> exerciseLogs) {
        this.mealLogs = mealLogs;
        this.exerciseLogs = exerciseLogs;
    }

    public List<MealLog> getMealLogs() {
        return mealLogs;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }
}
