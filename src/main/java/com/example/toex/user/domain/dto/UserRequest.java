package com.example.toex.user.domain.dto;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long userId;
    private String name;
    private String email;

    // 필요한 경우 다른 필드 및 메서드 추가
}