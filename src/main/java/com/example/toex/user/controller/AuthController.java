package com.example.toex.user.controller;


import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.common.message.BasicResponse;
import com.example.toex.jwt.JwtAuthenticationProvider;
import com.example.toex.user.domain.dto.TokenInfo;
import com.example.toex.user.domain.dto.UserResponse;
import com.example.toex.user.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final OAuthService oAuthService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<BasicResponse<Void>>  logout(HttpServletRequest request) {
        oAuthService.logout(request);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }


    /**
     * 탈퇴
     */

    @DeleteMapping("/withdraw")
    public  ResponseEntity<BasicResponse<Void>> withdraw(HttpServletRequest request) {
        oAuthService.withdraw(request);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }

    /**
     * 토큰 갱신
     */
    @PatchMapping("/user/refresh")
    public ResponseEntity<BasicResponse<TokenInfo>> refreshLogin(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if (refreshToken == null || !jwtAuthenticationProvider.verifyRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String newAccessToken = jwtAuthenticationProvider.regenerateAccessToken(refreshToken);
        String newRefreshToken = jwtAuthenticationProvider.regenerateRefreshToken(refreshToken);
        TokenInfo tokenInfo = new TokenInfo(newAccessToken, newRefreshToken);
        return ResponseEntity.ok(BasicResponse.ofSuccess(tokenInfo));
    }

    /**
     * 로그인/회원가입
     */


    // 카카오 로그인
    @PostMapping("/login/kakao")
    public ResponseEntity<BasicResponse<UserResponse>> loginKakao(@RequestParam("code") String authorizationCode) {
        UserResponse userResponse = oAuthService.loginKakao(authorizationCode);
        return ResponseEntity.ok(BasicResponse.ofSuccess(userResponse));
    }


    //구글 로그인
    @GetMapping("/login/google")
    public ResponseEntity<BasicResponse<UserResponse>> loginGoogle(@RequestParam("code") String authorizationCode) {
        UserResponse userResponse =  oAuthService.loginGoogle(authorizationCode);
        return ResponseEntity.ok(BasicResponse.ofSuccess(userResponse));
    }

    //네이버 로그인
    @GetMapping("/login/naver")
    public ResponseEntity<BasicResponse<UserResponse>> loginNaver(@RequestParam("code") String authorizationCode, String state) {
        UserResponse userResponse =oAuthService.loginNaver(authorizationCode,state);
        return ResponseEntity.ok(BasicResponse.ofSuccess(userResponse));
    }
}
