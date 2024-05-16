package com.example.toex.common.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse<T>{
    private String message;
    private Integer statusCode;
    private T data;

    private static final String SUCCESS = "Success";

    public static <T> BasicResponse<T> of(HttpStatus statusCode, String message, T data) {
        return new BasicResponse<>(message, statusCode.value(), data);
    }

    public static <T> BasicResponse<T> ofSuccess(T data){
        return new BasicResponse<>(SUCCESS, HttpStatus.OK.value(), data);
    }

    public static <T> BasicResponse<T> ofCreateSuccess(T data){
        return new BasicResponse<>(SUCCESS, HttpStatus.CREATED.value(), data);
    }
}
