package com.example.test.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequest {
    @Email
    private String email;
    @Size(min = 4, max = 15)
    private String password;
}
