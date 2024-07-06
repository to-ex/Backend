package com.example.toex.user.domain.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverInfoResponse implements UserInfo {
    private String resultcode;
    private String message;
    private Response response;

    @Override
    public String getEmail() {
        return response != null ? response.getEmail() : null;
    }

    @Override
    public String getName() {
        return response != null ? response.getName() : null;
    }


    @Data
    public static class Response {
        private String id;
        private String email;
        private String name;
    }
}
