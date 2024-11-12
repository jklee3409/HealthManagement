package com.healthmanagement.personalized_health_service.service;

import com.healthmanagement.personalized_health_service.model.ExerciseLog;
import com.healthmanagement.personalized_health_service.model.MealLog;
import com.healthmanagement.personalized_health_service.repository.ExerciseLogRepository;
import com.healthmanagement.personalized_health_service.repository.MealLogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private MealLogRepository mealLogRepository;

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    public MealLog addMealLog(MealLog mealLog) {
        return mealLogRepository.save(mealLog);
    }

    public ExerciseLog addExerciseLog(ExerciseLog exerciseLog) {
        return exerciseLogRepository.save(exerciseLog);
    }

    public List<MealLog> getMealLogsByUserId(Long userId) {
        return mealLogRepository.findByUserId(userId);
    }

    public List<ExerciseLog> getExerciseLogsByUserId(Long userId) {
        return exerciseLogRepository.findByUserId(userId);
    }
}
