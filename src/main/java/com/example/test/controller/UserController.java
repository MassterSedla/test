package com.example.test.controller;

import com.example.test.model.Task;
import com.example.test.model.User;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> showUser(@PathVariable Long id) {
        return new ResponseEntity<>(service.findUserById(id), HttpStatus.OK);
    }
}
