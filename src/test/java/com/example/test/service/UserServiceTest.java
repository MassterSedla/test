package com.example.test.service;

import com.example.test.dto.RegistrationUserDto;
import com.example.test.model.Role;
import com.example.test.model.User;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;
    private User user1;
    private String username;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);

        email = "user1@gmail.com";
        username = "user1";
        password = "user1";

        Set<Role> roles = Collections
                .singleton(new Role(1L, "ROLE_USER"));

        user1 = new User(email, username, password, roles);
        user1.setId(1L);
    }

    @Test
    void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user1);
        RegistrationUserDto registrationUserDto =
                new RegistrationUserDto(email, username, password, password);
        user = userService.saveUser(registrationUserDto);
        assertNotNull(user);
        assertEquals(user, user1);
    }

    @Test
    void findUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        user = userService.findUserById(1L);
        assertNotNull(user);
        assertEquals(user, user1);
    }

    @Test
    void findUserByEmail() {
        when(userRepository.findByEmail(email)).thenReturn(user1);
        user = userService.findUserByEmail(email);
        assertNotNull(user);
        assertEquals(user, user1);
    }

    @Test
    void findUserByUsername() {
        when(userRepository.findByUsername(username)).thenReturn(user1);
        user = userService.findUserByUsername(username);
        assertNotNull(user);
        assertEquals(user, user1);
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(user1);
        when(userRepository.findAll()).thenReturn(users);
        List<User> users1 = userService.getAllUsers();
        assertNotNull(users1);
        assertEquals(users1, users);
    }

    @Test
    void loadUserByUsernamePositiveTest() {
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        user1.getEmail(),
                        user1.getPassword(),
                        user1.getAuthorities()
                );
        when(userRepository.findByEmail(email)).thenReturn(user1);
        UserDetails userDetails1 = userService.loadUserByUsername(email);
        assertNotNull(userDetails1);
        assertEquals(userDetails1, userDetails);
    }

    @Test
    void loadUserByUsernameNegativeTest() {
        String badEmail = "!";
        when(userRepository.findByEmail(badEmail)).thenReturn(null);
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(badEmail));
        assertEquals("User with email ! not found", exception.getMessage());
    }
}