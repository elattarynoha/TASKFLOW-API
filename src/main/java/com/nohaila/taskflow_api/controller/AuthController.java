package com.nohaila.taskflow_api.controller;

import com.nohaila.taskflow_api.dto.AuthResponse;
import com.nohaila.taskflow_api.dto.LoginRequest;
import com.nohaila.taskflow_api.dto.RegisterRequest;
import com.nohaila.taskflow_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}