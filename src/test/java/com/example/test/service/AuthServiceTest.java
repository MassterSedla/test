package com.example.test.service;

import com.example.test.dto.JwtRequest;
import com.example.test.dto.JwtResponse;
import com.example.test.dto.RegistrationUserDto;
import com.example.test.model.Role;
import com.example.test.model.User;
import com.example.test.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    private ResponseEntity<?> response;
    private Set<Role> roles;
    private User user;
    private JwtRequest request;
    private JwtRequest badRequest;
    private String username;
    private String email;
    @BeforeEach
    void setUp() {
        email = "user1@gmail.com";
        username = "user1";
        String password = "user1";


        roles = Collections
                .singleton(new Role(1L, "ROLE_USER"));

        user = new User(email, username, password, roles);

        RegistrationUserDto userDto = new RegistrationUserDto(
                email, username, password, password);

        lenient().when(userService.saveUser(userDto)).thenReturn(user);

        request = new JwtRequest(email, password);
        badRequest = new JwtRequest("", "");
    }

    @Test
    void createAuthTokenPositiveTest() {
        when(userService.findUserByEmail(email)).thenReturn(user);
        response = authService.createAuthToken(request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), username);
    }

    @Test
    void createAuthTokenNegativeTest() {
        lenient().when(userService.findUserByEmail(badRequest.getEmail()))
                .thenReturn(null);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        badRequest.getEmail(), badRequest.getPassword()
                )
        )).thenThrow(BadCredentialsException.class);
        response = authService.createAuthToken(badRequest);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertTrue(response.getBody().toString()
                .contains("Incorrect email or password"));
    }

    @Test
    void createNewUserPositiveTest() {
        User user2;
        User user1;
        String email2 = "user2@gmail.com";
        String username2 = "user2";
        String password = "user2";
        user1 = new User(email2, username2, password, roles);
        RegistrationUserDto userDto =
                new RegistrationUserDto(email2,
                        username2, password, password);
        when(userService.saveUser(userDto)).thenReturn(user1);
        lenient().when(userService.findUserByEmail(email2)).thenReturn(null);
        lenient().when(userService.findUserByUsername(username2)).thenReturn(null);
        response = authService.createNewUser(userDto);
        user2 = (User) response.getBody();
        assertNotNull(user2);
        assertEquals(user1, user2);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(user2.getUsername(), username2);
        assertEquals(user2.getEmail(), email2);

    }

    @Test
    void createNewUserNegativeTest() {
        RegistrationUserDto userDto1 =
                new RegistrationUserDto("user2@gmail.com",
                        "user2", "user", "user2");
        RegistrationUserDto userDto2 =
                new RegistrationUserDto("user2@gmail.com",
                        "user1", "user", "user");
        RegistrationUserDto userDto3 =
                new RegistrationUserDto("user1@gmail.com",
                        "user2", "user", "user");
        lenient().when(userService.findUserByEmail(email)).thenReturn(user);
        lenient().when(userService.findUserByUsername(username)).thenReturn(user);
        response = authService.createNewUser(userDto1);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("Password mismatch"));
        response = authService.createNewUser(userDto2);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("A user with the specified username already exists"));
        response = authService.createNewUser(userDto3);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().toString()
                .contains("A user with the specified email already exists"));

    }
}