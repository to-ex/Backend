package com.example.toex.board.domain;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private Long userId;

    private String title;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Enumerated(EnumType.STRING)
    private CountryTag countryTag;

    @Lob
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImg> boardImgs = new ArrayList<>();

    @Builder
    public Board(BoardReq req, Long userId) {
        this.userId = userId;
        this.title = req.getTitle();
        this.boardCategory = req.getBoardCategory();
        this.countryTag = req.getCountryTag();
        this.content = req.getContent();
    }

    public void setBoardImgs(List<BoardImg> boardImgs) {
        boardImgs.forEach(boardImg -> boardImg.setBoard(this));
    }
}
