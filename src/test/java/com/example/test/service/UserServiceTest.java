//package com.example.test.service;
//
//import com.example.test.repository.CommentRepository;
//import com.example.test.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userService = new UserService(userRepository, passwordEncoder);
//
//    }
//
//    @Test
//    void saveUser() {
//    }
//
//    @Test
//    void findUserById() {
//    }
//
//    @Test
//    void findUserByEmail() {
//    }
//
//    @Test
//    void findUserByUsername() {
//    }
//
//    @Test
//    void saveComment() {
//    }
//
//    @Test
//    void getAllUsers() {
//    }
//
//    @Test
//    void getComment() {
//    }
//
//    @Test
//    void loadUserByUsername() {
//    }
//}