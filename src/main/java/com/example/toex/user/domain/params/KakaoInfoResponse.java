package com.example.toex.user.domain.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoInfoResponse implements UserInfo {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile")
        private Profile profile;

        @Data
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;
        }
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getName() {
        return kakaoAccount.getProfile().getNickname();
    }
}
