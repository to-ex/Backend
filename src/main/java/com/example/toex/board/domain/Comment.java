package com.example.toex.board.domain;

import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.dto.req.CommentReq;
import com.example.toex.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    private Integer commentSeq;

    private Long commenterId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Comment(CommentReq req, Long userId, Board board) {
        this.content = req.getContent();
        this.commentSeq = 0; // 기본값 설정, 필요에 따라 변경
        this.commenterId = userId;
        this.board = board;
    }

    public void updateComment(CommentReq req) {
        this.content = req.getContent();
    }
}
