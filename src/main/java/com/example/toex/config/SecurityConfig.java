package com.example.toex.config;

import com.example.toex.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public static final String[] PERMIT_ALL_REQUESTS = {
            "/",  "/swagger-ui/**", "/v3/api-docs/**","/api/v1/auth/login/**","/api/v1/auth/user/refresh",
            "/api/v1/engTest", "/api/v1/board/**","/api/v1/user/myinfo"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpSecurity -> httpSecurity.disable())
                .csrf(httpSecurity -> httpSecurity.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/board").permitAll() // GET 요청은 인증 없이 허용
                                .requestMatchers("/api/v1/engTest/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/board/**").permitAll() // GET 요청은 인증 없이 허용
                                .requestMatchers(HttpMethod.POST, "/api/v1/board/").authenticated() // POST 요청은 인증 필요
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/board/**").authenticated() // PATCH 요청은 인증 필요
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/board/**").authenticated() // DELETE 요청은 인증 필요
                                .requestMatchers("/api/v1/board/mypost", "/api/v1/board/scrap", "/api/v1/like/**", "/api/v1/scrap/**", "/api/v1/comment/**").authenticated() // 인증 필요
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous(anonymous -> anonymous.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("PATCH");

        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
