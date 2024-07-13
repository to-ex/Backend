package com.example.toex.user.domain.dto;


import com.example.toex.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateRequest {
    private String name;
    private String email;

    private String useImage;
}
