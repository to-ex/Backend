package com.example.toex.board.service;

import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.security.CustomUserDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    Long insertBoard(BoardReq boardReq, List<MultipartFile> images, CustomUserDetail userDetail);
}
