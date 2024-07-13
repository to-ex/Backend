package com.example.toex.board.service;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.dto.req.CommentReq;
import com.example.toex.board.dto.res.BoardDetailRes;
import com.example.toex.board.dto.res.BoardRes;
import com.example.toex.security.CustomUserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    Long insertBoard(BoardReq boardReq, List<MultipartFile> images, CustomUserDetail userDetail);
    Page<BoardRes> getBoardList(Pageable pageable, String keyword, BoardCategory boardCategory, CountryTag countryTag, CustomUserDetail userDetail);
    BoardDetailRes getBoardDetail(Pageable pageable, Long boardId, CustomUserDetail userDetail);
    Page<BoardRes> getMyPosts(Pageable pageable, CustomUserDetail userDetail);
    Page<BoardRes> getMyScraps(Pageable pageable, CustomUserDetail userDetail);
    Long updateBoard(Long boardId, BoardReq boardReq, CustomUserDetail userDetail);
    Long deleteBoard(Long boardId, CustomUserDetail userDetail);
    void toggleLike(Long boardId, CustomUserDetail userDetail);
    void toggleScrap(Long boardId, CustomUserDetail userDetail);
    Long createComment(Long boardId, CommentReq commentReq, CustomUserDetail userDetail); //commentID 반환
    void deleteComment(Long commentId, CustomUserDetail userDetail);
    Long updateComment(Long commentId, CommentReq commentReq, CustomUserDetail userDetail);
}
