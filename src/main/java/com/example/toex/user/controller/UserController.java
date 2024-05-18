package com.example.toex.user.controller;


import com.example.toex.user.domain.dto.LoginResponse;
import com.example.toex.user.service.OAuthService;

import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final OAuthService oAuthService;

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
}
