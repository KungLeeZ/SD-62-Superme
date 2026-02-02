package com.example.sd_62.user.repository;

import com.example.sd_62.user.entity.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOAuthRepository extends JpaRepository<UserOAuth, Integer> {

    // Tìm OAuth account bằng provider và providerId
    Optional<UserOAuth> findByProviderAndProviderId(String provider, String providerId);

    // Tìm OAuth accounts của user
    List<UserOAuth> findByUserId(Integer userId);

    // Tìm OAuth account của user theo provider
    Optional<UserOAuth> findByUserIdAndProvider(Integer userId, String provider);

    // Kiểm tra OAuth account đã tồn tại
    boolean existsByProviderAndProviderId(String provider, String providerId);

    // Đếm số OAuth accounts của user
    long countByUserId(Integer userId);
}