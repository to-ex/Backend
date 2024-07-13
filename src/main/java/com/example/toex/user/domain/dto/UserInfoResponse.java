package com.example.toex.user.domain.dto;


import com.example.toex.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private Long userId;
    private String name;
    private String email;
    private String userImage;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userImage = user.getUserImage();

    }
}