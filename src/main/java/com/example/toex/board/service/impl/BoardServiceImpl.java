package com.example.toex.board.service.impl;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.BoardImg;
import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.dto.res.BoardDetailRes;
import com.example.toex.board.dto.res.BoardRes;
import com.example.toex.board.repository.BoardRepository;
import com.example.toex.board.service.BoardService;
import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.common.file.FileService;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    @Transactional
    @Override
    public Long insertBoard(BoardReq boardReq, List<MultipartFile> images, CustomUserDetail userDetail) {
        if (userDetail == null || userDetail.getUser() == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Board board = boardRepository.save(Board.builder()
                .req(boardReq)
                .userId(userDetail.getUser().getUserId())
                .build());

        List<BoardImg> boardImgList = new ArrayList<>();
        images.forEach(image -> {
            try {
                String filePath = fileService.uploadFile(image);
                boardImgList.add(BoardImg.builder().imgUrl(filePath).build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        board.setBoardImgs(boardImgList);

        return board.getBoardId();
    }

    @Override
    public Page<BoardRes> getBoardList(Pageable pageable, String keyword, BoardCategory boardCategory, CountryTag countryTag, CustomUserDetail userDetail) {
        Long userId = null;
        if (userDetail != null) {
            userId = userDetail.getUser().getUserId();
        }

        List<BoardRes> boardResList = boardRepository.selectBoardList(keyword, boardCategory, countryTag, userId);

        return pageImplCustom(boardResList, pageable);
    }

    @Override
    public BoardDetailRes getBoardDetail(Pageable pageable, Long boardId, CustomUserDetail userDetail) {
        Long userId = null;
        if (userDetail != null) {
            userId = userDetail.getUser().getUserId();
        }

        BoardDetailRes boardDetailRes = boardRepository.selectBoardDetail(boardId, userId);
        if (boardDetailRes == null) {
            throw new CustomException(ErrorCode.INVALID_BOARD);
        }

        List<BoardDetailRes.CommentRes> commentResList = boardRepository.selectCommentList(boardId);
        boardDetailRes.setCommentList(pageImplCustom(commentResList, pageable));

        return boardDetailRes;
    }

    @Override
    public Long updateBoard(Long boardId, BoardReq boardReq, CustomUserDetail userDetail) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BOARD));

        if (userDetail == null || !board.getUserId().equals(userDetail.getUser().getUserId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        board.updateBoard(boardReq);

        return boardRepository.save(board).getBoardId();
    }

    @Override
    public Long deleteBoard(Long boardId, CustomUserDetail userDetail) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BOARD));

        if (userDetail == null || !board.getUserId().equals(userDetail.getUser().getUserId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        board.delete();

        return boardRepository.save(board).getBoardId();
    }

    public <T> Page<T> pageImplCustom(List<T> list, org.springframework.data.domain.Pageable pageable) {
        int start = (int) pageable.getOffset();

        if (start >= list.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, list.size());
        }

        int end = Math.min((start + pageable.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
