package com.example.toex.board.domain;

import com.example.toex.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "scraps")
public class Scraps extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    private Long userId;

    private Long boardId;

    @Builder
    public Scraps(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }
}
