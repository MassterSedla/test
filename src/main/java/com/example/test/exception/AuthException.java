package com.example.test.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthException extends Exception{
    private int status;
    private String message;
    private Date timestamp;

    public AuthException(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
