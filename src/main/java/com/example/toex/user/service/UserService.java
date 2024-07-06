package com.example.toex.user.service;

import com.example.toex.user.domain.dto.UserInfoResponse;
import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
import com.example.toex.user.User;
import com.example.toex.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserInfoResponse(user);
    }

    public UserInfoResponse updateUserInfo(Long userId, UserInfoUpdateRequest userInfoUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.update(userInfoUpdateRequest);
        return new UserInfoResponse(user);
    }

    public UserInfoResponse getUserPage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserInfoResponse(user);
    }
}
