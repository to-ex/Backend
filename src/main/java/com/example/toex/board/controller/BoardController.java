package com.example.toex.board.controller;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.service.BoardService;
import com.example.toex.common.message.BasicResponse;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<?> createBoard(@RequestPart(value = "boardReq") BoardReq boardReq,
                                         @RequestPart(value = "images" ,required = false) List<MultipartFile> images,
                                         @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(boardService.insertBoard(boardReq, images, userDetail)));
    }

    @GetMapping("/board")
    public ResponseEntity<?> getBoardList(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "boardCategory" ,required = false) BoardCategory boardCategory,
                                          @RequestParam(value = "countryTag" ,required = false) CountryTag countryTag,
                                         @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoardList(pageable, keyword, boardCategory, countryTag, userDetail)));
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardList(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                          @PathVariable Long boardId,
                                          @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoardDetail(pageable, boardId, userDetail)));
    }

}
