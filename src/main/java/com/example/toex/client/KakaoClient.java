package com.example.toex.client;

import com.example.toex.user.domain.params.KakaoInfoResponse;
import com.example.toex.user.domain.params.KakaoTokens;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class KakaoClient {

    private static final Logger logger = LoggerFactory.getLogger(KakaoClient.class);

    @Value("${security.oauth2.client.registration.kakao.client_id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.kakao.redirect_uri}")
    private String redirectUri;

    @Value("${security.oauth2.client.registration.kakao.client_secret}")
    private String clientSecret;
    private final RestTemplate restTemplate;

    // 카카오 서버에 access token 요청
    public String requestAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode);

        String requestUri = "https://kauth.kakao.com/oauth/token";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        logger.debug("Requesting Kakao access token with authorization code: {}", authorizationCode);
        KakaoTokens response = restTemplate.postForObject(requestUri, request, KakaoTokens.class);



        return response.getAccessToken();
    }

    // 카카오 서버에 사용자 정보 요청
    public KakaoInfoResponse requestKakaoInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestUri = "https://kapi.kakao.com/v2/user/me";

        HttpEntity<Void> request = new HttpEntity<>(headers);

        logger.debug("Requesting Kakao user info with access token: {}", accessToken);
        KakaoInfoResponse response = restTemplate.postForObject(requestUri, request, KakaoInfoResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to retrieve user info.");
        }

        return response;
    }
}
