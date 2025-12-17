package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.UserDTO;
import com.kwewk.course_project.dto.request.LoginRequest;
import com.kwewk.course_project.dto.request.RegisterRequest;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.security.AuthenticationResponse;
import com.kwewk.course_project.service.AuthService;
import com.kwewk.course_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse authResponse = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO user = userService.getUserByName(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/user/by-name")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByName(@RequestParam String name) {
        UserDTO user = userService.getUserByName(name);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/user/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> userExists(@PathVariable Long id) {
        boolean exists = userService.userExists(id);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}