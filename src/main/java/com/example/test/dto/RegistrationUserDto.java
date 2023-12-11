package com.example.test.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RegistrationUserDto {
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 4, max = 15)
    private String username;

    @Size(min = 4, max = 15)
    private String password;

    @Size(min = 4, max = 15)
    private String confirmPassword;

    public RegistrationUserDto() {
    }
}
