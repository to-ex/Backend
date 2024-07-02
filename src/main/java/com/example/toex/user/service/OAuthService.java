package com.example.toex.user.service;

import com.example.toex.client.GoogleClient;
import com.example.toex.client.KakaoClient;
import com.example.toex.client.NaverClient;
import com.example.toex.common.exception.CustomErrorException;
import com.example.toex.jwt.JwtAuthenticationProvider;
import com.example.toex.user.User;
import com.example.toex.user.domain.dto.UserResponse;
import com.example.toex.user.domain.params.GoogleInfoResponse;
import com.example.toex.user.domain.params.KakaoInfoResponse;
import com.example.toex.user.domain.params.NaverInfoResponse;
import com.example.toex.user.domain.params.UserInfo;

import com.example.toex.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public UserResponse loginKakao(String authorizationCode) {
        String accessToken = kakaoClient.requestAccessToken(authorizationCode);
        KakaoInfoResponse info = kakaoClient.requestKakaoInfo(accessToken);
        return loginCommon(info);
    }

    public UserResponse loginNaver(String authorizationCode, String state) {
        String accessToken = naverClient.requestAccessToken(authorizationCode, state);
        NaverInfoResponse info = naverClient.requestNaverInfo(accessToken);
        return loginCommon(info);
    }

    public UserResponse loginGoogle(String authorizationCode) {
        String accessToken = googleClient.requestAccessToken(authorizationCode);
        GoogleInfoResponse info = googleClient.requestGoogleInfo(accessToken);
        return loginCommon(info);
    }


    private <T extends UserInfo> UserResponse loginCommon(T info) {
        User user = findOrCreateMember(info);
        return UserResponse.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(jwtAuthenticationProvider.createAccessToken(user.getUserId(), user.getEmail()))
                .refreshToken(user.getRefreshToken())
                .build();
    }

    private <T extends UserInfo> User findOrCreateMember(T info) {
        return userRepository.findByEmail(info.getEmail())
                .orElseGet(() -> newMember(info));
    }

    private <T extends UserInfo> User newMember(T info) {
        String refreshToken = jwtAuthenticationProvider.createRefreshToken(null, info.getEmail());
        User user = User.builder()
                .email(info.getEmail())
                .name(info.getName())
                .refreshToken(refreshToken)
                .build();
        userRepository.save(user);
        return user;
    }

    // 로그아웃 메서드

    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.invalidateRefreshToken();
        userRepository.save(user);
    }
}
