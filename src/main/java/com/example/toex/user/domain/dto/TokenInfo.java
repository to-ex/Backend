package com.example.toex.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
}