package com.example.sd_62.user.service;

import com.example.sd_62.user.dto.request.LoginRequest;
import com.example.sd_62.user.dto.request.RegisterRequest;
import com.example.sd_62.user.dto.response.AuthResponse;
import com.example.sd_62.user.entity.User;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken, Integer userId);

    boolean validateToken(String token);

    // Thêm phương thức này
    void saveRefreshToken(User user, String refreshToken);
}
