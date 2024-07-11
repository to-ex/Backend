package com.example.toex.board.domain;

import com.example.toex.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "likes")
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    private Long userId;

    private Long boardId;

    @Builder
    public Likes(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    public void update() {
        if (this.delYn.equals("N")) {
            this.delYn = "Y";
        } else {
            this.delYn = "N";
        }
    }
}
