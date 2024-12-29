package com.springboot.Payload;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuccessResponse<T> {

	private String status;
	private String message;
	private T data;
	private HttpStatus httpStatus;
	
	// Constructor for specified fields
    public SuccessResponse(String status, String message, HttpStatus httpStatus) {
        this.status = status;
        this.message = message;     
        this.httpStatus = httpStatus;
    }
    


}
