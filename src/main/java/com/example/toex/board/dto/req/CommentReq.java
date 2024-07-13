package com.example.toex.board.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReq {
    private Long commentId;
    private String content;
}
