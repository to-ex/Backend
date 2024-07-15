package com.example.toex.user.controller;

import com.example.toex.common.file.FileService;
import com.example.toex.security.CustomUserDetail;
import com.example.toex.user.domain.dto.UserInfoResponse;
import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
import com.example.toex.user.service.*;
import com.example.toex.jwt.JwtAuthenticationProvider;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/mypage")
public class UserController {

    private final OAuthService oAuthService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserService userService;

    private final FileService fileService;


    // 내 정보관리 페이지 조회
    @GetMapping
    public UserInfoResponse getMyInfo() {
        Long userId = jwtAuthenticationProvider.getUserId();
        System.out.println("userId = " + userId);
        return userService.getUserInfo(userId);
    }

    // 내 정보관리 페이지 수정
    @PatchMapping
    public UserInfoResponse updateMyInfo(@RequestPart(value = "name") String name,
                                         @RequestPart(value = "email") String email,
                                         @RequestPart(value = "image", required = false) MultipartFile image,
                                         @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {
        UserInfoUpdateRequest userInfoUpdateRequest = new UserInfoUpdateRequest(name, email);

        return userService.updateUserInfo(userInfoUpdateRequest, image, userDetail);
    }

    //닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity checkNicknameDuplicate(@RequestParam String newName) {
        boolean isDuplicate = userService.isNicknameDuplicate(newName);
        return ResponseEntity.ok(isDuplicate);
    }

}
