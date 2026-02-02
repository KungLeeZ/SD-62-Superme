package com.example.sd_62.user.service;

import com.example.sd_62.user.entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenService {
    
    // Tạo refresh token mới
    RefreshToken createRefreshToken(Integer userId);
    
    // Tìm refresh token bằng token
    Optional<RefreshToken> findByToken(String token);
    
    // Tìm refresh token hợp lệ của user
    Optional<RefreshToken> findValidTokenByUser(Integer userId);
    
    // Thu hồi token
    void revokeToken(String token);
    
    // Thu hồi tất cả tokens của user
    void revokeAllUserTokens(Integer userId);
    
    // Xóa tokens đã hết hạn
    void deleteExpiredTokens();
    
    // Lấy tất cả tokens của user
    List<RefreshToken> getUserTokens(Integer userId);
    
    // Kiểm tra token có hợp lệ không
    boolean isValidToken(String token);
    
    // Cập nhật expiry time
    RefreshToken updateExpiry(String token, LocalDateTime newExpiry);
}