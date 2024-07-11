package com.example.toex.board.service.impl;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.BoardImg;
import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
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
        Long userId = (long) 1;
        if (userDetail != null) {
            userId = userDetail.getUser().getUserId();
        }

        List<BoardRes> boardResList = boardRepository.selectBoardList(keyword, boardCategory, countryTag, userId);

        int start = (int) pageable.getOffset();

        if (start >= boardResList.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, boardResList.size());
        }

        int end = Math.min(start + pageable.getPageSize(), boardResList.size());

        return new PageImpl<>(boardResList.subList(start, end), pageable, boardResList.size());

    }
}
