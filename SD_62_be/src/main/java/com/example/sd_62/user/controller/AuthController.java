package com.example.sd_62.user.controller;

import com.example.sd_62.user.dto.request.LoginRequest;
import com.example.sd_62.user.dto.response.AuthResponse;
import com.example.sd_62.user.service.AuthService;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
