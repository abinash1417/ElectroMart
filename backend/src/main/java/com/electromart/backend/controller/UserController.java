package com.electromart.backend.controller;

import com.electromart.backend.dto.AuthResponse;
import com.electromart.backend.dto.LoginDTO;
import com.electromart.backend.dto.UserDTO;
import com.electromart.backend.model.User;
import com.electromart.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User created = userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponse response = userService.loginUser(
                loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(response);
    }
}