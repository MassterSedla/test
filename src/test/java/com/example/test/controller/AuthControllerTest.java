//package com.example.test.controller;
//
//import com.example.test.dto.JwtRequest;
//import com.example.test.dto.RegistrationUserDto;
//import com.example.test.service.AuthService;
//import com.example.test.util.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.event.annotation.BeforeTestMethod;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.http.HttpStatus.OK;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
//
//@WebMvcTest(AuthController.class)
//@ExtendWith(MockitoExtension.class)
//class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//    private JwtRequest validAuthRequest;
//    private RegistrationUserDto validRegistrationDto;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = standaloneSetup(authController).build();
//        validAuthRequest = new JwtRequest("user1@gmail.com", "user");
//        validRegistrationDto = new RegistrationUserDto("user1@gmail.com",
//                "user", "user", "user");
//    }
//
//    @Test
//    void shouldCreateAuthTokenWhenValidRequest() throws Exception {
//        when(authController.createAuthToken(validAuthRequest).getStatusCode()).thenReturn(OK);
//
//        mockMvc.perform(get("/auth"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldCreateNewUserWhenValidRequest() {
//        ResponseEntity<?> response = authController.createNewUser(validRegistrationDto);
//
//        assertEquals(OK, response.getStatusCode());
//        verify(authService).createNewUser(validRegistrationDto);
//    }
//}