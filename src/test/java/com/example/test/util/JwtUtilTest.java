package com.example.test.util;

import com.example.test.model.Role;
import com.example.test.model.User;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private JwtUtil jwtUtil;
    private User user;
    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setRoleRepository(roleRepository);
        Role role = new Role(1L, "ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user = new User("user1@gmail.com", "user", "user", roles);
        lenient().when(roleRepository.save(role)).thenReturn(role);
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByUsername("user")).thenReturn(user);
        ReflectionTestUtils.setField(jwtUtil, "secret", "test");
        ReflectionTestUtils.setField(jwtUtil, "lifetime", Duration.ofMinutes(30));
    }

    @Test
    void generatedToken() {
        String token = jwtUtil.generatedToken(user);
        assertNotNull(token);
        assertEquals(jwtUtil.getUsernameFromToken(token), user.getUsername());
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtUtil.generatedToken(user);
        String username = jwtUtil.getUsernameFromToken(token);
        assertNotNull(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void getRoleFromToken() {
        String token = jwtUtil.generatedToken(user);
        List<String> roles = jwtUtil.getRoleFromToken(token);
        assertNotNull(roles);
        assertEquals(roles, user.getRoles().stream().map(Role::getName).toList());
    }
}