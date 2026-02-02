package com.example.sd_62.user.service.impl;

import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.entity.UserOAuth;
import com.example.sd_62.user.repository.UserOAuthRepository;
import com.example.sd_62.user.repository.UserRepository;
import com.example.sd_62.user.service.UserOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserOAuthServiceImpl implements UserOAuthService {
    
    private final UserOAuthRepository userOAuthRepository;
    private final UserRepository userRepository;
    
    @Override
    public UserOAuth linkOAuthAccount(Integer userId, String provider, String providerId, String email) {
        log.info("Linking OAuth account for user id: {}, provider: {}", userId, provider);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        
        // Kiểm tra đã tồn tại chưa
        if (existsByProviderAndProviderId(provider, providerId)) {
            throw new RuntimeException("OAuth account đã tồn tại");
        }
        
        // Tạo OAuth account
        UserOAuth userOAuth = new UserOAuth();
        userOAuth.setUser(user);
        userOAuth.setProvider(provider);
        userOAuth.setProviderId(providerId);
        userOAuth.setMailProvider(email);
        userOAuth.setCreateAt(LocalDateTime.now());
        
        UserOAuth savedOAuth = userOAuthRepository.save(userOAuth);
        log.info("Linked OAuth account successfully for user: {}", user.getEmail());
        
        return savedOAuth;
    }
    
    @Override
    public void unlinkOAuthAccount(Integer oauthId) {
        log.info("Unlinking OAuth account with id: {}", oauthId);
        
        UserOAuth userOAuth = userOAuthRepository.findById(oauthId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy OAuth account với ID: " + oauthId));
        
        userOAuthRepository.delete(userOAuth);
        log.info("Unlinked OAuth account successfully");
    }
    
    @Override
    public Optional<UserOAuth> findByProviderAndProviderId(String provider, String providerId) {
        return userOAuthRepository.findByProviderAndProviderId(provider, providerId);
    }
    
    @Override
    public List<UserOAuth> getUserOAuthAccounts(Integer userId) {
        return userOAuthRepository.findByUserId(userId);
    }
    
    @Override
    public boolean existsByProviderAndProviderId(String provider, String providerId) {
        return userOAuthRepository.existsByProviderAndProviderId(provider, providerId);
    }
    
    @Override
    public Optional<UserOAuth> getUserOAuthByProvider(Integer userId, String provider) {
        return userOAuthRepository.findByUserIdAndProvider(userId, provider);
    }
    
    @Override
    public long countUserOAuthAccounts(Integer userId) {
        return userOAuthRepository.countByUserId(userId);
    }
}