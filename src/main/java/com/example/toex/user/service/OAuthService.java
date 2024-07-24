package com.example.toex.user.service;

import com.example.toex.board.domain.Board;
import com.example.toex.board.repository.BoardRepository;
import com.example.toex.board.repository.CommentRepository;
import com.example.toex.board.repository.LikesRepository;
import com.example.toex.board.repository.ScrapsRepository;
import com.example.toex.client.GoogleClient;
import com.example.toex.client.KakaoClient;
import com.example.toex.client.NaverClient;
import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.jwt.JwtAuthenticationProvider;
import com.example.toex.schedule.repository.ScheduleRepository;
import com.example.toex.user.User;
import com.example.toex.user.domain.dto.UserResponse;
import com.example.toex.user.domain.params.GoogleInfoResponse;
import com.example.toex.user.domain.params.KakaoInfoResponse;
import com.example.toex.user.domain.params.NaverInfoResponse;
import com.example.toex.user.domain.params.UserInfo;
import com.example.toex.user.respository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class OAuthService {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final LikesRepository likesRepository;
    private final ScrapsRepository scrapsRepository;


    private static final String DEFAULT_USER_IMAGE_URL = "https://toex-file.s3.ap-northeast-2.amazonaws.com/userImages/default+profile.png";

    public UserResponse loginKakao(String authorizationCode) {
        String accessToken = kakaoClient.requestAccessToken(authorizationCode);
        KakaoInfoResponse info = kakaoClient.requestKakaoInfo(accessToken);
        return loginCommon(info);
    }

    public UserResponse loginNaver(String authorizationCode, String state) {
        String accessToken = naverClient.requestAccessToken(authorizationCode, state);
        NaverInfoResponse info = naverClient.requestNaverInfo(accessToken);
        return loginCommon(info);
    }

    public UserResponse loginGoogle(String authorizationCode) {
        String accessToken = googleClient.requestAccessToken(authorizationCode);
        GoogleInfoResponse info = googleClient.requestGoogleInfo(accessToken);
        return loginCommon(info);
    }

    private <T extends UserInfo> UserResponse loginCommon(T info) {
        User user = findOrCreateMember(info);
        String newAccessToken = jwtAuthenticationProvider.createAccessToken(user.getUserId(), user.getEmail());
        String newRefreshToken = jwtAuthenticationProvider.createRefreshToken(user.getUserId(), user.getEmail());

        // Refresh Token 갱신
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userImage(user.getUserImage())
                .build();
    }

    private <T extends UserInfo> User findOrCreateMember(T info) {
        return userRepository.findByEmailAndDelYn(info.getEmail(), "N")
                .orElseGet(() -> newMember(info));
    }
    private <T extends UserInfo> User newMember(T info) {
        if (userRepository.findByEmailAndDelYn(info.getEmail(), "N").isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }

        String refreshToken = jwtAuthenticationProvider.createRefreshToken(null, info.getEmail());

        User user = User.builder()
                .email(info.getEmail())
                .name(info.getName())
                .refreshToken(refreshToken)
                .userImage(DEFAULT_USER_IMAGE_URL)
                .build();
        userRepository.save(user);
        return user;
    }


    // 로그아웃 메서드
    public void logout(HttpServletRequest request) {
        String accessToken = jwtAuthenticationProvider.extract(request);
        Claims claims = jwtAuthenticationProvider.verify(accessToken);
        String email = claims.getSubject();

        User user = userRepository.findByEmailAndDelYn(email, "N")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.invalidateRefreshToken();
        userRepository.save(user);
    }

    // 탈퇴 메서드
    public void withdraw(HttpServletRequest request) {
        String accessToken = jwtAuthenticationProvider.extract(request);
        Claims claims = jwtAuthenticationProvider.verify(accessToken);
        Long userId = jwtAuthenticationProvider.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        // 회원이 작성한 게시글 및 해당 게시글의 댓글 삭제
        List<Board> boardList = boardRepository.findByUserId(user.getUserId());
        boardList.forEach(board -> {
            commentRepository.deleteAll(board.getComments());
            likesRepository.deleteAll(likesRepository.findByBoardId(board.getBoardId()));
            scrapsRepository.deleteAll(scrapsRepository.findByBoardId(board.getBoardId()));
        });
        boardRepository.deleteAll(boardList);

        // 회원이 작성한 댓글 삭제
        commentRepository.deleteAll(commentRepository.findByCommenterId(user.getUserId()));

        // 회원 스케줄 삭제
        scheduleRepository.deleteAll(scheduleRepository.findByUserId(user.getUserId()));

        user.softDelete(); // 사용자 소프트 삭제
        userRepository.save(user);
    }
}
