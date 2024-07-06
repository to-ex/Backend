package com.example.toex.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // JWT
    NOT_MATCHING_TOKEN (401, "JWT001", "토큰이 일치하지 않습니다."),
    NO_REFRESH_TOKEN (400, "JWT002", "refresh 토큰이 존재하지 않습니다."),
    INVALID_TOKEN(403, "JWT003", "유효하지 않은 토큰입니다."),
    NOT_MATCHING_REFRESH_TOKEN(403, "JWT004", "저장된 refresh 토큰과 일치하지 않습니다."),

    REFRESH_TOKEN_EXPIRED(403,"JWT004","refresh Token 이 만료되었습니다"),

    //USER
    ALREADY_EXIST_USER(409, "USER001", "이미 존재하는 사용자입니다."),
    INVALID_USER(404, "USER002", "존재하지 않는 사용자입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "USER003", "잘못된 비밀번호입니다."),

    // ROLE
    ACCESS_DENIED (403, "ROLE001", "접근 권한이 없습니다."),

    // BOARD
    INVALID_BOARD(404, "BOARD001", "존재하지 않는 게시물입니다."),
    ACTIVE_BOARD(403, "BOARD002", "삭제되지 않은 게시물입니다. 복구할 필요 없습니다."),
    ALREADY_LIKED(409, "LIKE001", "이미 좋아요를 누른 게시물입니다."),
    NOT_LIKED(403, "LIKE002", "좋아요를 누르지 않은 게시물입니다. 삭제할 수 없습니다."),


    // COMMENT
    INVALID_COMMENT(404, "COMMENT001", "존재하지 않는 댓글입니다."),
    ;

    private int status;
    private final String code;
    private final String message;
}
