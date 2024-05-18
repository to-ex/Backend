package com.example.toex.user.service;

import com.example.toex.client.GoogleClient;
import com.example.toex.client.KakaoClient;
import com.example.toex.client.NaverClient;
import com.example.toex.jwt.JwtAuthenticationProvider;
import com.example.toex.user.User;
import com.example.toex.user.domain.dto.LoginResponse;
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

    public LoginResponse loginKakao(String authorizationCode) {
        String accessToken = kakaoClient.requestAccessToken(authorizationCode);
        KakaoInfoResponse info = kakaoClient.requestKakaoInfo(accessToken);
        return loginCommon(info);
    }

    public LoginResponse loginNaver(String authorizationCode, String state) {
        String accessToken = naverClient.requestAccessToken(authorizationCode, state);
        NaverInfoResponse info = naverClient.requestNaverInfo(accessToken);
        return loginCommon(info);
    }

    public LoginResponse loginGoogle(String authorizationCode) {
        String accessToken = googleClient.requestAccessToken(authorizationCode);
        GoogleInfoResponse info = googleClient.requestGoogleInfo(accessToken);
        return loginCommon(info);
    }

    private <T extends UserInfo> LoginResponse loginCommon(T info) {
        Long userId = findOrCreateMember(info);
        return LoginResponse.builder()
                .id(userId)
                .name(info.getName())
                .email(info.getEmail())
                .accessToken(jwtAuthenticationProvider.createAccessToken(userId, info.getName()))
                .refreshToken(jwtAuthenticationProvider.createRefreshToken(userId, info.getName()))
                .build();
    }

    private <T extends UserInfo> Long findOrCreateMember(T info) {
        return userRepository.findByEmail(info.getEmail())
                .map(User::getUserId)
                .orElseGet(() -> newMember(info));
    }

    private <T extends UserInfo> Long newMember(T info) {
        User user = User.builder()
                .email(info.getEmail())
                .name(info.getName())
                .build();
        userRepository.save(user);
        return user.getUserId();
    }
}
