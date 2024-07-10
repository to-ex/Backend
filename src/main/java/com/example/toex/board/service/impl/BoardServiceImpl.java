package com.example.toex.board.service.impl;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.BoardImg;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.repository.BoardRepository;
import com.example.toex.board.service.BoardService;
import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.common.file.FileService;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
}
