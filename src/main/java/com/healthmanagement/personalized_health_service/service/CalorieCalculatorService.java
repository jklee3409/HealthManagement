package com.healthmanagement.personalized_health_service.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CalorieCalculatorService {

    private static final Map<String, Double> MET_VALUES;

    static {
        MET_VALUES = new HashMap<>();
        MET_VALUES.put("걷기", 3.5);
        MET_VALUES.put("조깅", 7.0);
        MET_VALUES.put("수영", 8.0);
        MET_VALUES.put("달리기", 11.0);
        MET_VALUES.put("자전거", 8.0);
        MET_VALUES.put("요가", 2.0);
        MET_VALUES.put("필라테스", 3.0);
        MET_VALUES.put("에어로빅", 4.0);
        MET_VALUES.put("등산", 8.0);
        MET_VALUES.put("줄넘기", 8.0);
        MET_VALUES.put("테니스", 5.0);
        MET_VALUES.put("배드민턴", 4.5);
        MET_VALUES.put("농구", 6.5);
        MET_VALUES.put("축구", 7.0);
        MET_VALUES.put("풋살", 6.5);
        MET_VALUES.put("웨이트 트레이닝", 5.0);
        MET_VALUES.put("헬스", 5.0);
    }

    public double calculateCalories(String exerciseType, double duration, double weight) {
        double durationHours = duration / 60;
        Double met = MET_VALUES.getOrDefault(exerciseType, 3.5);

        return met * weight * durationHours;
    }
}
