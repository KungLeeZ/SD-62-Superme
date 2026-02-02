package com.example.sd_62.user.service.impl;

import com.example.sd_62.user.entity.RefreshToken;
import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.repository.RefreshTokenRepository;
import com.example.sd_62.user.repository.UserRepository;
import com.example.sd_62.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    @Override
    public RefreshToken createRefreshToken(Integer userId) {
        log.info("Creating refresh token for user id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        
        // Tạo token string
        String token = UUID.randomUUID().toString();
        
        // Tạo refresh token entity
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryAt(LocalDateTime.now().plusDays(7)); // 7 days
        refreshToken.setRevoked(false);
        
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token for user: {}", user.getEmail());
        
        return savedToken;
    }
    
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Override
    public Optional<RefreshToken> findValidTokenByUser(Integer userId) {
        return refreshTokenRepository.findValidTokenByUser(userId, LocalDateTime.now());
    }
    
    @Override
    public void revokeToken(String token) {
        log.info("Revoking token: {}", token);
        
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                    log.info("Revoked token successfully");
                });
    }
    
    @Override
    public void revokeAllUserTokens(Integer userId) {
        log.info("Revoking all tokens for user id: {}", userId);
        refreshTokenRepository.revokeAllUserTokens(userId);
        log.info("Revoked all tokens for user id: {}", userId);
    }
    
    @Override
    public void deleteExpiredTokens() {
        log.info("Deleting expired tokens");
        LocalDateTime now = LocalDateTime.now();
        refreshTokenRepository.deleteExpiredTokens(now);
        log.info("Deleted expired tokens");
    }
    
    @Override
    public List<RefreshToken> getUserTokens(Integer userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
    
    @Override
    public boolean isValidToken(String token) {
        return refreshTokenRepository.isValidToken(token, LocalDateTime.now());
    }
    
    @Override
    public RefreshToken updateExpiry(String token, LocalDateTime newExpiry) {
        log.info("Updating expiry for token: {}", token);
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token không tồn tại"));
        
        refreshToken.setExpiryAt(newExpiry);
        RefreshToken updatedToken = refreshTokenRepository.save(refreshToken);
        log.info("Updated token expiry successfully");
        
        return updatedToken;
    }
    
    // Phương thức cleanup định kỳ
    public void cleanup() {
        deleteExpiredTokens();
        
        // Có thể thêm logic cleanup khác
        List<RefreshToken> revokedTokens = refreshTokenRepository.findByRevokedTrue();
        if (!revokedTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(revokedTokens);
            log.info("Cleaned up {} revoked tokens", revokedTokens.size());
        }
    }
}