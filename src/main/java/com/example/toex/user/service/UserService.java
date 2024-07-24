package com.example.toex.user.service;

import com.example.toex.board.domain.BoardImg;
import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.common.file.FileService;
import com.example.toex.security.CustomUserDetail;
import com.example.toex.user.domain.dto.UserInfoResponse;
import com.example.toex.user.domain.dto.UserInfoUpdateRequest;
import com.example.toex.user.User;
import com.example.toex.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserInfoResponse(user);
    }

    public UserInfoResponse updateUserInfo(UserInfoUpdateRequest userInfoUpdateRequest, MultipartFile image, CustomUserDetail userDetail) {
        Long userId = getUserId(userDetail, false);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
                String filePath = fileService.uploadFile(image,"userImages");
                user.update(userInfoUpdateRequest,filePath);
                userRepository.save(user);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return new UserInfoResponse(user);
    }

    public UserInfoResponse getUserPage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserInfoResponse(user);
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

    //닉네임 중복 확인
    public boolean isNicknameDuplicate(String name) {
        Optional<User> user = userRepository.findByName(name);
        return user.isPresent();
    }

}
