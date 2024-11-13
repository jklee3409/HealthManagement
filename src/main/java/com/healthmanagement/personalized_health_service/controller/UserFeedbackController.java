package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.service.ChatGptApiClient;
import com.healthmanagement.personalized_health_service.service.UserFeedbackService;
import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;
    private final UserService userService;

    @Autowired
    public UserFeedbackController(UserFeedbackService userFeedbackService, UserService userService) {
        this.userFeedbackService = userFeedbackService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getFeedback(@RequestParam Long userId) {
        // 사용자 정보 가져오기
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 맞춤형 피드백 생성
        String feedback = userFeedbackService.generateFeedback(user);
        return ResponseEntity.ok(feedback);
    }
}
