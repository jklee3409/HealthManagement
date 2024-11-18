package com.healthmanagement.personalized_health_service.service;

import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.model.UserFeedback;
import com.healthmanagement.personalized_health_service.repository.ExerciseLogRepository;
import com.healthmanagement.personalized_health_service.repository.MealLogRepository;
import com.healthmanagement.personalized_health_service.repository.UserFeedbackRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFeedbackService {

    private final ChatGptApiClient chatGptApiClient;
    private final MealLogRepository mealLogRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserFeedbackRepository userFeedbackRepository;
    private final UserService userService;

    @Autowired
    public UserFeedbackService(ChatGptApiClient chatGptApiClient,
                               MealLogRepository mealLogRepository,
                               ExerciseLogRepository exerciseLogRepository,
                               UserFeedbackRepository userFeedbackRepository,
                               UserService userService) {
        this.chatGptApiClient = chatGptApiClient;
        this.mealLogRepository = mealLogRepository;
        this.exerciseLogRepository = exerciseLogRepository;
        this.userFeedbackRepository = userFeedbackRepository;
        this.userService = userService;
    }

    public String generateFeedback(User user) {
        String prompt = createPrompt(user);
        String feedback = chatGptApiClient.getRecommendation(prompt);
        saveFeedback(user.getId(), feedback);
        return feedback;
    }

    public void saveFeedback(Long userId, String feedback) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserFeedback userFeedback = new UserFeedback(user, feedback, LocalDateTime.now());
        userFeedbackRepository.save(userFeedback);
    }


    public List<UserFeedback> getFeedbacksByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return userFeedbackRepository.findByUserIdAndTimestampBetween(userId, start, end);
    }

    private String createPrompt(User user) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("User ").append(user.getUsername()).append("의 목표는").append(user.getGoal()).append("입니다.\n");
        prompt.append("최근 식단: ");

        mealLogRepository.findTop5ByUserOrderByDateDesc(user).forEach(mealLog -> {
            prompt.append(mealLog.getFoodName()).append(" (").append(mealLog.getCalories()).append(" 칼로리), ");
        });

        prompt.append("\n 최근 운동 기록: ");
        exerciseLogRepository.findTop5ByUserOrderByDateDesc(user).forEach(exerciseLog -> {
            prompt.append(exerciseLog.getExerciseName()).append(" (").append(exerciseLog.getExerciseTime()).append("분), ");
        });

        prompt.append("\n위 데이터를 바탕으로 맞춤형 건강 피드백을 생성해주세요.");
        return prompt.toString();
    }
}
