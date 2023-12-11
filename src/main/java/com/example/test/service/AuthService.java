package com.example.test.service;

import com.example.test.dto.JwtRequest;
import com.example.test.dto.JwtResponse;
import com.example.test.dto.RegistrationUserDto;
import com.example.test.exception.AuthException;
import com.example.test.model.User;
import com.example.test.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthException(HttpStatus.UNAUTHORIZED.value(),
                    "Incorrect email or password"), HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUserByEmail(authRequest.getEmail());
        String token = jwtUtil.generatedToken(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        user.getAuthorities()
                )
        );
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(), "Password mismatch"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByUsername(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "A user with the specified username already exists\n"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByEmail(registrationUserDto.getEmail()) != null) {
            return new ResponseEntity<>(new AuthException(HttpStatus.BAD_REQUEST.value(),
                    "A user with the specified email already exists\n"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.saveUser(registrationUserDto);
        return ResponseEntity.ok(user);
    }
}