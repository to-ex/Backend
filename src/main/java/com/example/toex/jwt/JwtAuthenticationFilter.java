package com.example.toex.jwt;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.message.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String token = jwtAuthenticationProvider.extract(request);

        log.info("Incoming request to: {}", request.getRequestURI());
        log.info("JWT Token: {}", token);

        try {
            // access token 유효성 체크
            if (!Objects.isNull(token)) verifyTokenAndSetAuthentication(token);
            filterChain.doFilter(request, response);
        }
        catch (CustomException e) {
            logger.error(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
            this.responseSend(response, errorResponse);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getClass().getName(), e.getClass().getName());
            this.responseSend(response, errorResponse);
        }
    }

    private HttpServletResponse responseSend (HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertObjectToJson(errorResponse));
        return response;
    }

    private void verifyTokenAndSetAuthentication(String token){
        Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) { return null; }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
