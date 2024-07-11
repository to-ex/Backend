package com.example.toex.board.service.impl;

import com.example.toex.board.domain.Board;
import com.example.toex.board.domain.BoardImg;
import com.example.toex.board.domain.Likes;
import com.example.toex.board.domain.Scraps;
import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.req.BoardReq;
import com.example.toex.board.dto.res.BoardDetailRes;
import com.example.toex.board.dto.res.BoardRes;
import com.example.toex.board.repository.BoardRepository;
import com.example.toex.board.repository.LikesRepository;
import com.example.toex.board.repository.ScrapsRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;
    private final ScrapsRepository scrapsRepository;
    private final FileService fileService;

    @Transactional
    @Override
    public Long insertBoard(BoardReq boardReq, List<MultipartFile> images, CustomUserDetail userDetail) {
        Long userId = getUserId(userDetail, true);

        Board board = boardRepository.save(Board.builder()
                .req(boardReq)
                .userId(userId)
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
        Long userId = getUserId(userDetail, false);

        List<BoardRes> boardResList = boardRepository.selectBoardList(keyword, boardCategory, countryTag, userId, false);

        return pageImplCustom(boardResList, pageable);
    }

    @Override
    public BoardDetailRes getBoardDetail(Pageable pageable, Long boardId, CustomUserDetail userDetail) {
        Long userId = getUserId(userDetail, false);

        BoardDetailRes boardDetailRes = boardRepository.selectBoardDetail(boardId, userId);
        if (boardDetailRes == null) {
            throw new CustomException(ErrorCode.INVALID_BOARD);
        }

        List<BoardDetailRes.CommentRes> commentResList = boardRepository.selectCommentList(boardId);
        boardDetailRes.setCommentList(pageImplCustom(commentResList, pageable));

        return boardDetailRes;
    }

    @Override
    public Page<BoardRes> getMyPosts(Pageable pageable, CustomUserDetail userDetail) {
//        Long userId = getUserId(userDetail, true);

        List<BoardRes> boardResList = boardRepository.selectBoardList(null, null, null, 1L, true);
        return pageImplCustom(boardResList, pageable);
    }

    @Override
    public Page<BoardRes> getMyScraps(Pageable pageable, CustomUserDetail userDetail) {
//        Long userId = getUserId(userDetail, true);
        List<BoardRes> boardResList = boardRepository.selectMyScraps(1L);
        return pageImplCustom(boardResList, pageable);
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

    @Override
    public void toggleLike(Long boardId, CustomUserDetail userDetail) {
//        Long userId = getUserId(userDetail, true);
        Long userId = 1L;
        Optional<Likes> likesOptional = likesRepository.findByUserIdAndBoardId(userId, boardId);
        if (likesOptional.isEmpty()) {
            likesRepository.save(Likes.builder().userId(userId).boardId(boardId).build());
        } else {
            Likes likes = likesOptional.get();
            likes.update();
            likesRepository.save(likes);
        }
    }

    @Override
    public void toggleScrap(Long boardId, CustomUserDetail userDetail) {
        //        Long userId = getUserId(userDetail, true);
        Long userId = 1L;
        Optional<Scraps> scrapsOptional = scrapsRepository.findByUserIdAndBoardId(userId, boardId);
        if (scrapsOptional.isEmpty()) {
            scrapsRepository.save(Scraps.builder().userId(userId).boardId(boardId).build());
        } else {
            Scraps scraps = scrapsOptional.get();
            scraps.update();
            scrapsRepository.save(scraps);
        }
    }

    public Long getUserId(CustomUserDetail userDetail, Boolean authcheck) {
        if (userDetail == null || userDetail.getUser() == null) {
            if (authcheck) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            return null;
        }
        return userDetail.getUser().getUserId();
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
