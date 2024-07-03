package com.example.toex.jwt;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.user.User;
import com.example.toex.user.respository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;
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
        if (authorization != null && authorization.toLowerCase().startsWith(SCHEME.toLowerCase())) {
            return authorization.substring(SCHEME.length()).trim();
        }
        return null;
    }

    public String createAccessToken(Long userId, String email) {
        log.info("Creating Access Token for userId: {}, email: {}", userId, email);
        return createToken(userId, email, "Access", accessTokenTime);
    }

    public String createRefreshToken(Long userId, String email) {
        log.info("Creating Refresh Token for userId: {}, email: {}", userId, email);
        return createToken(userId, email, "Refresh", refreshTokenTime);
    }

    public String createToken(Long userId, String email, String type, Long tokenValidTime) {
        log.info("Creating Token - Type: {}, userId: {}, email: {}", type, userId, email);
        return Jwts.builder()
                .setHeaderParam("type", type)
                .setClaims(createClaims(userId, email))
                .setSubject(email)
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .compact();
    }

    public static Claims createClaims(Long userId, String email) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("email", email);
        log.info("UserId from Claims: {}", userId);
        log.info("Email from Claims: {}", email);
        return claims;
    }

    public Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = extract(request);
        log.info("Extracted access token: {}", accessToken);

        if (accessToken != null) {
            Claims claims = verify(accessToken);
            log.info("Claims from token: {}", claims);
            log.info("User ID from claims: {}", claims.get("userId"));

            return Long.parseLong(claims.get("userId").toString());
        }
        return null;
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
        log.info("Extracted email from token: {}", email);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // Refresh token 검증
    public Claims verifyRefreshToken(String refreshToken) {
        try {
            Claims claims = verify(refreshToken);
            if (!claims.get("type").equals("Refresh")) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // Access token 재발급
    public String regenerateAccessToken(String refreshToken) {
        Claims claims = verifyRefreshToken(refreshToken);
        Long userId = Long.parseLong(claims.get("userId").toString());
        String email = claims.get("email").toString();
        return createAccessToken(userId, email);
    }

    // Refresh token 재발급
    public String regenerateRefreshToken(String accessToken) {
        Claims claims = verify(accessToken);
        Long userId = Long.parseLong(claims.get("userId").toString());
        String email = claims.get("email").toString();
        return createRefreshToken(userId, email);
    }
}
