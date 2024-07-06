package com.example.toex.client;

import com.example.toex.user.domain.params.GoogleInfoResponse;
import com.example.toex.user.domain.params.GoogleTokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class GoogleClient {

    private static final Logger logger = LoggerFactory.getLogger(GoogleClient.class);

    @Value("${security.oauth2.client.registration.google.client_id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.google.client_secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.google.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // access token 요청
    public String requestAccessToken(String authorizationCode) {
        // 인증 코드를 URL 디코딩
        authorizationCode = decodeAuthCode(authorizationCode);

        logger.debug("Requesting access token with authorization code: {}", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        String requestUri = "https://oauth2.googleapis.com/token";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUri, request, String.class);

            // 응답 내용을 출력하여 문제 파악
            logger.debug("Response status: {}", responseEntity.getStatusCode());
            logger.debug("Response body: {}", responseEntity.getBody());

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                String response = responseEntity.getBody();

                // JSON 응답을 GoogleTokens 객체로 매핑
                GoogleTokens tokens = objectMapper.readValue(response, GoogleTokens.class);
                if (tokens.getAccessToken() == null) {
                    throw new RuntimeException("Failed to retrieve access token.");
                }

                return tokens.getAccessToken();
            } else {
                throw new RuntimeException("Failed to retrieve access token. Response: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException | IOException e) {
            logger.error("Request failed with status: {} and body: {}", e.getMessage(), e.getLocalizedMessage());
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }

    // Google 서버에 사용자 정보 요청
    public GoogleInfoResponse requestGoogleInfo(String accessToken) {
        logger.debug("Requesting user info with access token: {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestUri = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleInfoResponse> responseEntity = restTemplate.exchange(requestUri, HttpMethod.GET, request, GoogleInfoResponse.class);

            logger.debug("Response status: {}", responseEntity.getStatusCode());
            logger.debug("Response body: {}", responseEntity.getBody());

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("Failed to retrieve user info. Response: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Request failed with status: {} and body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    // 인증 코드를 URL 디코딩하는 메서드
    private String decodeAuthCode(String authCode) {
        try {
            return URLDecoder.decode(authCode, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to decode authorization code", e);
        }
    }
}
