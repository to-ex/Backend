package com.example.toex.user.domain.dto;


import com.example.toex.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoUpdateRequest {
    private String name;
    private String email;

}
