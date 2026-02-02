package com.example.sd_62.user.service;

import com.example.sd_62.user.entity.UserOAuth;

import java.util.List;
import java.util.Optional;

public interface UserOAuthService {
    
    // Liên kết OAuth account với user
    UserOAuth linkOAuthAccount(Integer userId, String provider, String providerId, String email);
    
    // Hủy liên kết OAuth account
    void unlinkOAuthAccount(Integer oauthId);
    
    // Tìm OAuth account bằng provider và providerId
    Optional<UserOAuth> findByProviderAndProviderId(String provider, String providerId);
    
    // Tìm OAuth accounts của user
    List<UserOAuth> getUserOAuthAccounts(Integer userId);
    
    // Kiểm tra OAuth account đã tồn tại
    boolean existsByProviderAndProviderId(String provider, String providerId);
    
    // Lấy OAuth account của user theo provider
    Optional<UserOAuth> getUserOAuthByProvider(Integer userId, String provider);
    
    // Đếm số OAuth accounts của user
    long countUserOAuthAccounts(Integer userId);
}