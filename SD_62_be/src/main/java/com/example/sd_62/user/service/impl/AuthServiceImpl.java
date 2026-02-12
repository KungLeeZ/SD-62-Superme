package com.example.sd_62.user.service.impl;

import com.example.sd_62.configuration.security.CustomUserDetails;
import com.example.sd_62.configuration.security.JwtTokenProvider;
import com.example.sd_62.user.dto.request.LoginRequest;
import com.example.sd_62.user.dto.request.RegisterRequest;
import com.example.sd_62.user.dto.response.AuthResponse;
import com.example.sd_62.user.entity.RefreshToken;
import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserStatus;
import com.example.sd_62.user.repository.RefreshTokenRepository;
import com.example.sd_62.user.repository.UserGroupRepository;
import com.example.sd_62.user.repository.UserRepository;
import com.example.sd_62.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGroupRepository userGroupRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        try {
            // Xác thực credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Kiểm tra trạng thái tài khoản
            User user = userDetails.getUser();
            if (user.getStatus() == UserStatus.LOCKED) {
                throw new RuntimeException("Tài khoản đã bị khóa");
            }
            if (user.getStatus() == UserStatus.INACTIVE) {
                throw new RuntimeException("Tài khoản chưa được kích hoạt");
            }

            // Tạo tokens
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            // Lưu refresh token vào database
            saveRefreshToken(user, refreshToken);

            log.info("Login successful for email: {}", request.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400L) // 24 giờ
                    .user(user)
                    .build();

        } catch (Exception e) {
            log.error("Login failed for email: {}", request.getEmail(), e);
            throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Register attempt for email: {}", request.getEmail());

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Kiểm tra password
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        // ===== QUAN TRỌNG: TÌM HOẶC TẠO CUSTOMER GROUP =====
        UserGroup customerGroup = userGroupRepository.findByName("Khách hàng")
                .orElseGet(null);

        log.info("Assigning user to group: {} (ID: {})",
                customerGroup.getName(), customerGroup.getId());

        // Tạo user mới với group CUSTOMER
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setStatus(UserStatus.ACTIVE);
        user.setGroup(customerGroup);
        user.setEmailVerified(false);
        user.setAvatarUrl(null);

        User savedUser = userRepository.save(user);
        log.info("Registered successfully for email: {} with CUSTOMER role",
                request.getEmail());

        // Tự động login sau khi register
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());

        return login(loginRequest);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ");
        }

        // Kiểm tra trong database
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));

        if (storedToken.getRevoked()) {
            throw new RuntimeException("Refresh token đã bị thu hồi");
        }

        if (storedToken.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token đã hết hạn");
        }

        // Lấy user từ token
        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Kiểm tra trạng thái user
        if (user.getStatus() == UserStatus.LOCKED) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        // Tạo tokens mới
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Revoke old refresh token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // Lưu refresh token mới
        saveRefreshToken(user, newRefreshToken);

        log.info("Token refreshed successfully for email: {}", email);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(user)
                .build();
    }

    @Override
    public void logout(String refreshToken, Integer userId) {
        log.info("Logout for user id: {}", userId);

        // Revoke refresh token nếu có
        if (refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
        }

        // Revoke all user tokens
        if (userId != null) {
            refreshTokenRepository.revokeAllUserTokens(userId);
        }

        // Clear security context
        SecurityContextHolder.clearContext();

        log.info("Logout successful for user id: {}", userId);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiryAt(LocalDateTime.now().plusDays(7)); // 7 days
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);
        log.debug("Saved refresh token for user: {}", user.getEmail());
    }

    // Phương thức helper để lấy user hiện tại
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }

        return null;
    }

    // Phương thức helper để lấy user ID hiện tại
    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}