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
    private static final String[] EXCLUDE_URLS = {
            "/api/v1/auth/login/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/engTest",
            "/api/v1/auth/user/refresh",
            "/error" // 추가
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();

        for (String excludeUrl : EXCLUDE_URLS) {
            if (pathMatcher.match(excludeUrl, requestUri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String accessToken = jwtAuthenticationProvider.extract(request);
        String refreshToken = request.getHeader("RefreshToken");

        log.info("Incoming request to: {}", requestUri);
        log.info("JWT Access Token: {}", accessToken);
        log.info("JWT Refresh Token: {}", refreshToken);

        try {
            if (accessToken != null) {
                log.info("Attempting to verify Access Token");
                jwtAuthenticationProvider.verify(accessToken);
                verifyTokenAndSetAuthentication(accessToken);
            } else if (refreshToken != null && requestUri.equals("/api/v1/auth/user/refresh")) {
                log.info("Attempting to verify Refresh Token");
                jwtAuthenticationProvider.verifyRefreshToken(refreshToken);
                String newAccessToken = jwtAuthenticationProvider.regenerateAccessToken(refreshToken);
                response.setHeader("AccessToken", newAccessToken);
                verifyTokenAndSetAuthentication(newAccessToken);
            } else {
                log.warn("No Access Token or Refresh Token provided, throwing exception");
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("SecurityContextHolder before filterChain.doFilter: {}", auth);

            filterChain.doFilter(request, response);

            auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("SecurityContextHolder after filterChain.doFilter: {}", auth);
        } catch (CustomException e) {
            log.error("CustomException: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
            this.responseSend(response, errorResponse);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getClass().getName(), e.getMessage());
            this.responseSend(response, errorResponse);
        }
    }

    private void verifyTokenAndSetAuthentication(String token) {
        Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set for user: {}", authentication.getName());
            log.info("SecurityContextHolder contains: {}", SecurityContextHolder.getContext().getAuthentication());
        } else {
            log.warn("Failed to set authentication for token: {}", token);
        }
    }

    private HttpServletResponse responseSend(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertObjectToJson(errorResponse));
        return response;
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
