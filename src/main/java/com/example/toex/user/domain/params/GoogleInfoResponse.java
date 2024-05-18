package com.example.toex.user.domain.params;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoogleInfoResponse implements UserInfo {
    private String sub;
    private String email;
    private boolean email_verified;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;


    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}
