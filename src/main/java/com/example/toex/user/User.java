package com.example.toex.user;

import com.example.toex.common.BaseEntity;
import com.example.toex.user.domain.dto.UserInfoUpdateRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String name;
    private String refreshToken;
    private String userImage;

    @Column(name = "created_dt", nullable = false)
    @CreatedDate
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    @LastModifiedDate
    private LocalDateTime updatedDt;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;

    @Column(name = "del_yn", columnDefinition = "VARCHAR(1) default 'N'")
    private String delYn;

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

    @PrePersist
    public void prePersist() {
        if (this.createdDt == null) {
            this.createdDt = LocalDateTime.now();
        }
        if (this.updatedDt == null) {
            this.updatedDt = LocalDateTime.now();
        }
        this.delYn = "N";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDt = LocalDateTime.now();
    }

    public void softDelete() {
        this.delYn = "Y";
        this.deletedDt = LocalDateTime.now();
    }
    public boolean isDeleted() {
        return "Y".equals(this.delYn);
    }

}
