package com.example.toex.common.exception;

import com.example.toex.common.message.ErrorResponse;
import lombok.Getter;

@Getter
public class CustomErrorException extends RuntimeException{
    private ErrorResponse errorResponse;
    public CustomErrorException(ErrorResponse errorResponse){
        this.errorResponse = errorResponse;
    }
}
