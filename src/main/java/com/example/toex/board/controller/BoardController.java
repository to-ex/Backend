package com.example.toex.board.controller;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.service.BoardService;
import com.example.toex.common.message.BasicResponse;
import com.example.toex.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board")
    @Operation(summary = "게시글 작성", description = "게시글 작성")
    public ResponseEntity<?> createBoard(@RequestPart(value = "boardReq") BoardReq boardReq,
                                         @RequestPart(value = "images" ,required = false) List<MultipartFile> images,
                                         @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(boardService.insertBoard(boardReq, images, userDetail)));
    }
}
