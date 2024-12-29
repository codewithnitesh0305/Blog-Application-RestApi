package com.springboot.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtRequest {

	private String email;
    private String password;
}
