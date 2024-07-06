package com.example.toex.user;

import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
import io.jsonwebtoken.Claims;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String name;
    private String refreshToken;

    @Builder
    public User(Long userId, String email, String name, String refreshToken) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.refreshToken = refreshToken;
    }

    // 사용자 정보 업데이트 메서드
    public void update(UserInfoUpdateRequest userInfoUpdateRequest) {
        this.name = userInfoUpdateRequest.getName();
        this.email = userInfoUpdateRequest.getEmail();
    }


    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
