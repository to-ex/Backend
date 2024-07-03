package com.example.toex.jwt;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.common.message.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();

        // 특정 엔드포인트는 필터링 제외
        if (pathMatcher.match("/api/v1/auth/login/**", requestUri) || pathMatcher.match("/api/v1/auth/callback/**", requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtAuthenticationProvider.extract(request);
        String refreshToken = request.getHeader("RefreshToken");

        log.info("Incoming request to: {}", requestUri);
        log.info("JWT Access Token: {}", accessToken);
        log.info("JWT Refresh Token: {}", refreshToken);

        try {
            if (accessToken != null && refreshToken != null) {
                // Case 4: Access Token과 Refresh Token 모두 유효한 경우 -> 정상 처리
                jwtAuthenticationProvider.verify(accessToken);
                jwtAuthenticationProvider.verifyRefreshToken(refreshToken);
                verifyTokenAndSetAuthentication(accessToken);
            } else if (accessToken == null && refreshToken != null) {
                // Case 2: Access Token은 만료됐지만, Refresh Token은 유효한 경우 -> Refresh Token을 검증하여 Access Token 재발급
                jwtAuthenticationProvider.verifyRefreshToken(refreshToken);
                String newAccessToken = jwtAuthenticationProvider.regenerateAccessToken(refreshToken);
                response.setHeader("AccessToken", newAccessToken);
                verifyTokenAndSetAuthentication(newAccessToken);
            } else if (accessToken != null && refreshToken == null) {
                // Case 3: Access Token은 유효하지만, Refresh Token은 만료된 경우 -> 재로그인 요구
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            } else {
                // Case 1: Access Token과 Refresh Token 모두가 만료된 경우 -> 에러 발생, 재로그인 요구
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            logger.error(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
            this.responseSend(response, errorResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getClass().getName(), e.getMessage());
            this.responseSend(response, errorResponse);
        }
    }

    private HttpServletResponse responseSend(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertObjectToJson(errorResponse));
        return response;
    }

    private void verifyTokenAndSetAuthentication(String token) {
        Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
