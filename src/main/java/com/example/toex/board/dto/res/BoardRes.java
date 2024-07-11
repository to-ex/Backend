package com.example.toex.board.dto.res;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardRes {
    private Long boardId;
    private String author;
    // TODO
    private String authorProfileImgUrl;
    private String title;
    private BoardCategory boardCategory;
    private CountryTag countryTag;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;
    private String imgUrl;
    private Long likes;
    private int comments;
    private Boolean isLiked;
    private Boolean isScrapped;

    @Builder
    public BoardRes(Board board, User author, Long likes, int comments, Long likeId, Long scrapId) {
        this.boardId = board.getBoardId();
        this.author = author.getName();
        this.title = board.getTitle();
        this.boardCategory = board.getBoardCategory();
        this.countryTag = board.getCountryTag();
        this.createdDt = board.getCreatedDt();
        this.updatedDt = board.getUpdatedDt();
        this.imgUrl = board.getBoardImgs().get(0).getImgUrl();
        this.likes = likes;
        this.comments = comments;
        this.isLiked = (likeId != null);
        this.isScrapped = (scrapId != null);
    }
}
