package com.example.toex.board.controller;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.dto.req.CommentReq;
import com.example.toex.board.service.BoardService;
import com.example.toex.common.message.BasicResponse;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
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
    public ResponseEntity<?> getBoardList(@PageableDefault Pageable pageable,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "boardCategory" ,required = false) BoardCategory boardCategory,
                                          @RequestParam(value = "countryTag" ,required = false) CountryTag countryTag,
                                         @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoardList(pageable, keyword, boardCategory, countryTag, userDetail)));
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardList(@PageableDefault Pageable pageable,
                                          @PathVariable Long boardId,
                                          @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoardDetail(pageable, boardId, userDetail)));
    }

    @GetMapping("/board/mypost")
    public ResponseEntity<?> getMyPosts(@PageableDefault Pageable pageable,
                                          @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getMyPosts(pageable, userDetail)));
    }

    @GetMapping("/board/scrap")
    public ResponseEntity<?> getMyScraps(@PageableDefault Pageable pageable,
                                        @AuthenticationPrincipal CustomUserDetail userDetail){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getMyScraps(pageable, userDetail)));
    }

    @PatchMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @RequestBody BoardReq boardReq,
                                         @AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.updateBoard(boardId, boardReq, userDetail)));
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.deleteBoard(boardId, userDetail)));
    }

    @PostMapping("/like/{boardId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long boardId,
                                        @AuthenticationPrincipal CustomUserDetail userDetail) {
        boardService.toggleLike(boardId, userDetail);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }

    @PostMapping("/scrap/{boardId}")
    public ResponseEntity<?> toggleScrap(@PathVariable Long boardId,
                                         @AuthenticationPrincipal CustomUserDetail userDetail) {
        boardService.toggleScrap(boardId, userDetail);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }

    @PostMapping("/comment/{boardId}") // 댓글 등록
    public ResponseEntity<?> createComment(@PathVariable Long boardId,
                                           @RequestBody CommentReq commentReq,
                                           @AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(boardService.createComment(boardId, commentReq, userDetail)));
    }

    @DeleteMapping("/comment/{commentId}") // 댓글 삭제
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal CustomUserDetail userDetail) {
        boardService.deleteComment(commentId, userDetail);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }

    @PatchMapping("/comment/{commentId}") // 댓글 수정
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentReq commentReq,
                                           @AuthenticationPrincipal CustomUserDetail userDetail) {
        log.info("Received request to update comment with ID: {}", commentId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.updateComment(commentId, commentReq, userDetail)));
    }
}
