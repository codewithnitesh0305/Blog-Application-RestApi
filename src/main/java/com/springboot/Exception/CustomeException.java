package com.springboot.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomeException extends RuntimeException{
    private String status;
    private int statusCode;
    private String message;
    private Object object;
}
