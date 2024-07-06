package com.example.toex.user.controller;


import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.jwt.JwtAuthenticationProvider;
import com.example.toex.user.domain.dto.TokenInfo;
import com.example.toex.user.domain.dto.UserRequest;
import com.example.toex.user.domain.dto.UserResponse;
import com.example.toex.user.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final OAuthService oAuthService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        oAuthService.logout(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(HttpServletRequest request) {
        oAuthService.withdraw(request);
        return ResponseEntity.ok().build();
    }
    // 토큰 갱신
    @PatchMapping("/user/refresh")
    public ResponseEntity<TokenInfo> refreshLogin(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if (refreshToken == null || !jwtAuthenticationProvider.verifyRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String newAccessToken = jwtAuthenticationProvider.regenerateAccessToken(refreshToken);
        TokenInfo tokenInfo = new TokenInfo(newAccessToken, refreshToken);
        return ResponseEntity.ok(tokenInfo);
    }



    // 카카오 로그인
    @PostMapping("/login/kakao")
    public UserResponse loginKakao(@RequestParam("code") String authorizationCode) {
        return oAuthService.loginKakao(authorizationCode);
    }

    //구글 로그인
    @PostMapping("/login/google")
    public UserResponse loginGoogle(@RequestParam("code") String authorizationCode) {
        return oAuthService.loginGoogle(authorizationCode);
    }

    //네이버 로그인
    @PostMapping("/login/naver")
    public UserResponse loginNaver(@RequestParam("code") String authorizationCode, String state) {
        return oAuthService.loginNaver(authorizationCode,state);
    }


}
