package com.example.toex.user;

import com.example.toex.common.BaseEntity;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String name;
    private String refreshToken;
    private String userImage;

    @Builder
    public User(Long userId, String email, String name, String refreshToken,String userImage) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.refreshToken = refreshToken;
        this.userImage = userImage;
    }

    // 사용자 정보 업데이트 메서드
    public void update(UserInfoUpdateRequest userInfoUpdateRequest,String image) {
        this.name = userInfoUpdateRequest.getName();
        this.email = userInfoUpdateRequest.getEmail();
        this.userImage = image;
    }


    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
