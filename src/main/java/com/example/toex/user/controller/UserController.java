package com.example.toex.user.controller;

import com.example.toex.user.domain.dto.UserResponse;
import com.example.toex.user.domain.dto.UserInfoResponse;
import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
import com.example.toex.user.service.*;
import com.example.toex.jwt.JwtAuthenticationProvider;

import lombok.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final OAuthService oAuthService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserService userService;


    // 내 정보관리 페이지 조회
    @GetMapping("/myinfo")
    public UserInfoResponse getMyInfo() {
        Long userId = jwtAuthenticationProvider.getUserId();
        System.out.println("userId = " + userId);
        return userService.getUserInfo(userId);
    }

    // 내 정보관리 페이지 수정
    @PostMapping("/myinfo")
    public UserInfoResponse updateMyInfo(@RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        Long userId = jwtAuthenticationProvider.getUserId();
        return userService.updateUserInfo(userId, userInfoUpdateRequest);
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public UserInfoResponse getMyPage() {
        Long userId = jwtAuthenticationProvider.getUserId();
        return userService.getUserPage(userId);
    }
}
