package com.example.toex.jwt;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private static final String HEADER_NAME = "Authorization";
    private static final String SCHEME = "Bearer";
    private SecretKey key;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-time}")
    private Long accessTokenTime;

    @Value("${jwt.refresh-token-time}")
    private Long refreshTokenTime;

    @PostConstruct
    public void initialize() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_NAME);
        if (!Objects.isNull(authorization) && authorization.toLowerCase().startsWith(SCHEME.toLowerCase())) {
            String tokenValue = authorization.substring(SCHEME.length()).trim();
            int commaIndex = tokenValue.indexOf(',');
            if (commaIndex > 0) {
                tokenValue = tokenValue.substring(0, commaIndex);
            }
            return tokenValue;
        }
        return null;
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String name) {
        return createToken(userId, name, "Access", accessTokenTime);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId, String name) {
        return createToken(userId, name, "Refresh", refreshTokenTime);
    }

    public String createToken(Long userId, String name, String type, Long tokenValidTime) {
        return Jwts.builder()
                .setHeaderParam("type", type) // Header 구성
                .setClaims(createClaims(userId, name)) // Payload - Claims 구성
                .setSubject(userId.toString()) // Payload - Subject 구성
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .compact();
    }

    public static Claims createClaims(Long userId, String name) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("name", name);
        return claims;
    }

    public Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = request.getHeader("Authorization").split(" ")[1].trim();
        String id = getId(accessToken);
        return Long.parseLong(id);
    }

    public String getId(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    public Claims verify(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        String email = verify(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
