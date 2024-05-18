package com.example.toex.client;

import com.example.toex.user.domain.params.NaverInfoResponse;
import com.example.toex.user.domain.params.NaverTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class NaverClient {

    @Value("${security.oauth2.client.registration.naver.client_id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.naver.client_secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.naver.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    // Naver 서버에 access token 요청
    public String requestAccessToken(String authorizationCode, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode);
        body.add("state", state);

        String requestUri = "https://nid.naver.com/oauth2.0/token";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            NaverTokens response = restTemplate.postForObject(requestUri, request, NaverTokens.class);
            if (response == null || response.getAccessToken() == null) {
                throw new RuntimeException("Failed to obtain access token from Naver");
            }
            return response.getAccessToken();
        } catch (HttpClientErrorException e) {
            System.err.println("Naver API 요청 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }

    // Naver 서버에 사용자 정보 요청
    public NaverInfoResponse requestNaverInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestUri = "https://openapi.naver.com/v1/nid/me";

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            NaverInfoResponse response = restTemplate.postForObject(requestUri, request, NaverInfoResponse.class);
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("Naver API 요청 중 오류 발생: " + e.getMessage());

            throw e;
        }
    }
}
