package com.example.toex.board.dto.res;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.BoardImg;
import com.example.toex.board.domain.Comment;
import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardDetailRes {
    private String author;
    // TODO
    private String authorProfileImgUrl;
    private String title;
    private BoardCategory boardCategory;
    private CountryTag countryTag;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;
    private List<String> imgUrlList;
    private String content;
    private int likes;
    private int comments;
    private Boolean isLiked;
    private Boolean isScrapped;
    private Page<CommentRes> commentList;

    @Builder
    public BoardDetailRes(Board board, User author, Long likes, int comments, Long likeId, Long scrapId) {
        this.author = author.getName();
        this.title = board.getTitle();
        this.boardCategory = board.getBoardCategory();
        this.countryTag = board.getCountryTag();
        this.createdDt = board.getCreatedDt();
        this.updatedDt = board.getUpdatedDt();
        this.imgUrlList = board.getBoardImgs().stream().map(BoardImg::getImgUrl).collect(Collectors.toList());
        this.content = board.getContent();
        this.likes = likes.intValue();
        this.comments = comments;
        this.isLiked = (likeId != null);
        this.isScrapped = (scrapId != null);
    }

    public void setCommentList(Page<CommentRes> commentList) {
        this.commentList = commentList;
    }

    @Getter
    public static class CommentRes {
        private Long commentId;
        private Long commenterId;
        private String commenterName;
        private LocalDateTime creaatedDt;
        // TODO
        private String profileImgUrl;
        private String content;

        @Builder
        public CommentRes(Comment comment, User commenter) {
            this.commentId = comment.getCommentId();
            this.commenterId = commenter.getUserId();
            this.commenterName = commenter.getName();
            this.creaatedDt = comment.getCreatedDt();
            this.content = comment.getContent();
        }
    }

}
