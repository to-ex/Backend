package com.example.toex.user;

import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
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

    @Builder
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // 사용자 정보 업데이트 메서드
    public void update(UserInfoUpdateRequest userInfoUpdateRequest) {
        this.name = userInfoUpdateRequest.getName();
        this.email = userInfoUpdateRequest.getEmail();
    }
}
