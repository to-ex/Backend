package com.example.toex.user.controller;

import com.example.toex.user.domain.dto.LoginResponse;
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

    // 카카오 로그인
    @PostMapping("/oauth/kakao/login")
    public LoginResponse loginKakao(@RequestParam("code") String authorizationCode) {
        return oAuthService.loginKakao(authorizationCode);
    }

    //구글 로그인
    @PostMapping("/oauth/google/login")
    public LoginResponse loginGoogle(@RequestParam("code") String authorizationCode) {
        return oAuthService.loginGoogle(authorizationCode);
    }

    //네이버 로그인
    @PostMapping("/oauth/naver/login")
    public LoginResponse loginNaver(@RequestParam("code") String authorizationCode,String state) {
        return oAuthService.loginNaver(authorizationCode,state);
    }

    // 내 정보관리 페이지 조회
    @GetMapping("/myinfo")
    public UserInfoResponse getMyInfo() {
        Long userId = jwtAuthenticationProvider.getUserId();
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
