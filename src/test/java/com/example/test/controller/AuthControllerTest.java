package com.example.test.controller;

import com.example.test.config.JwtRequestFilter;
import com.example.test.dto.JwtRequest;
import com.example.test.dto.JwtResponse;
import com.example.test.dto.RegistrationUserDto;
import com.example.test.exception.AuthException;
import com.example.test.model.Role;
import com.example.test.model.User;
import com.example.test.service.AuthService;
import com.example.test.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .addFilters(jwtRequestFilter)
                .build();
    }

    @Test
    void createAuthTokenPositiveTest() throws Exception {
        JwtRequest validAuthRequest = new JwtRequest(
                "user@gmail.com", "user");
        ResponseEntity<JwtResponse> response = ResponseEntity.ok(new JwtResponse(""));

        when((ResponseEntity<JwtResponse>) authService.createAuthToken(validAuthRequest))
                .thenReturn(response);
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validAuthRequest)))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(status().isOk());
        verify(authService).createAuthToken(validAuthRequest);
    }

    @Test
    void createAuthTokenNegativeTest() throws Exception {
        JwtRequest validAuthRequest = new JwtRequest(
                "user1@gmail.com", "user1");
        ResponseEntity<AuthException> response = new ResponseEntity<>(
                new AuthException(HttpStatus.UNAUTHORIZED.value(),
                "Incorrect email or password"), HttpStatus.UNAUTHORIZED);

        when((ResponseEntity<AuthException>) authService.createAuthToken(validAuthRequest))
                .thenReturn(response);
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validAuthRequest)))
                .andExpect(jsonPath("$.message",
                        equalTo("Incorrect email or password")))
                .andExpect(status().is4xxClientError());
        verify(authService).createAuthToken(validAuthRequest);
    }

    @Test
    void createNewUserPositiveTest() throws Exception {
        RegistrationUserDto validRegistrationDto = new RegistrationUserDto(
                "user1@gmail.com","user1",
                "user", "user");
        ResponseEntity<User> response = ResponseEntity.ok(new User(
                "user1@gmail.com", "user1", "user",
                Collections.singleton(new Role(1L, "ROLE_USER"))));
        when((ResponseEntity<User>) authService.createNewUser(validRegistrationDto))
                .thenReturn(response);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validRegistrationDto)))
                .andExpect(jsonPath("$.username",
                        equalTo(validRegistrationDto.getUsername())))
                .andExpect(jsonPath("$.email",
                        equalTo(validRegistrationDto.getEmail())))
                .andExpect(status().isOk());
        verify(authService).createNewUser(validRegistrationDto);
    }

    @Test
    void createNewUserMismatchPassword() throws Exception {
        RegistrationUserDto validRegistrationDto = new RegistrationUserDto(
                "user1@gmail.com","user1",
                "user", "user2");
        ResponseEntity<AuthException> response = new ResponseEntity<>(
                new AuthException(HttpStatus.BAD_REQUEST.value(),
                        "Password mismatch"), HttpStatus.BAD_REQUEST);

        when((ResponseEntity<AuthException>) authService.createNewUser(validRegistrationDto))
                .thenReturn(response);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validRegistrationDto)))
                .andExpect(jsonPath("$.message",
                        equalTo("Password mismatch")))
                .andExpect(status().is4xxClientError());
        verify(authService).createNewUser(validRegistrationDto);
    }

    @Test
    void createNewUserUsernameAlreadyExists() throws Exception {
        RegistrationUserDto validRegistrationDto = new RegistrationUserDto(
                "user1@gmail.com","user",
                "user", "user");
        ResponseEntity<AuthException> response = new ResponseEntity<>(
                new AuthException(HttpStatus.BAD_REQUEST.value(),
                        "A user with the specified username already exists"),
                        HttpStatus.BAD_REQUEST);

        when((ResponseEntity<AuthException>) authService.createNewUser(validRegistrationDto))
                .thenReturn(response);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validRegistrationDto)))
                .andExpect(jsonPath("$.message",
                        equalTo("A user with the specified username already exists")))
                .andExpect(status().is4xxClientError());
        verify(authService).createNewUser(validRegistrationDto);
    }

    @Test
    void createNewUserEmailAlreadyExists() throws Exception {
        RegistrationUserDto validRegistrationDto = new RegistrationUserDto(
                "user@gmail.com","user1",
                "user", "user");
        ResponseEntity<AuthException> response = new ResponseEntity<>(
                new AuthException(HttpStatus.BAD_REQUEST.value(),
                        "A user with the specified email already exists"),
                HttpStatus.BAD_REQUEST);

        when((ResponseEntity<AuthException>) authService.createNewUser(validRegistrationDto))
                .thenReturn(response);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validRegistrationDto)))
                .andExpect(jsonPath("$.message",
                        equalTo("A user with the specified email already exists")))
                .andExpect(status().is4xxClientError());
        verify(authService).createNewUser(validRegistrationDto);
    }
}