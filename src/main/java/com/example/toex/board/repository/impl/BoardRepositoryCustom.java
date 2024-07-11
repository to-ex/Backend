package com.example.toex.board.repository.impl;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.res.BoardDetailRes;
import com.example.toex.board.dto.res.BoardRes;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardRes> selectBoardList(String keyword, BoardCategory boardCategory, CountryTag countryTag, Long userId, Boolean mypost);
    BoardDetailRes selectBoardDetail(Long boardId, Long userId);
    List<BoardRes> selectMyScraps(Long userId);
    List<BoardDetailRes.CommentRes> selectCommentList(Long boardId);
}
