package com.healthmanagement.personalized_health_service.service;

import com.healthmanagement.personalized_health_service.model.User;
import com.healthmanagement.personalized_health_service.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일이 존재하지 않습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public void updateUserMetrics(Long userId, Map<String, Object> updates) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(("사용자를 찾을 수 없습니다.")));

        if (updates.containsKey("weight")) {
            user.setWeight((Double) updates.get("weight"));
        }
        if (updates.containsKey("skeletalMuscleMass")) {
            user.setSkeletalMuscleMass((Double) updates.get("skeletalMuscleMass"));
        }

        userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
