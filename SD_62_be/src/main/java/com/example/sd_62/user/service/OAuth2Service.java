package com.example.sd_62.user.service;

import com.example.sd_62.configuration.security.CustomUserDetails;
import com.example.sd_62.configuration.security.JwtTokenProvider;
import com.example.sd_62.user.dto.response.OAuth2Response;
import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.entity.UserOAuth;
import com.example.sd_62.user.enums.UserGroupType;
import com.example.sd_62.user.repository.UserGroupRepository;
import com.example.sd_62.user.repository.UserOAuthRepository;
import com.example.sd_62.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserOAuthRepository userOAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Xử lý cho Google
        if ("google".equals(provider)) {
            return processGoogleUser(attributes);
        }

        throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + provider);
    }

    private OAuth2User processGoogleUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        String providerId = (String) attributes.get("sub");

        log.info("Processing Google OAuth2 user: {}", email);

        // Tìm user bằng email
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        boolean isNewUser = false;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            log.info("User already exists: {}", email);
        } else {
            // Tạo user mới từ OAuth2
            user = User.fromOAuth2(email, name, picture);

            // Gán default group (CUSTOMER)
            UserGroup defaultGroup = userGroupRepository.findByType(UserGroupType.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Default user group not found"));
            user.setGroup(defaultGroup);

            user = userRepository.save(user);
            isNewUser = true;
            log.info("Created new user from OAuth2: {}", email);
        }

        // Lưu/link OAuth provider info
        saveOrUpdateOAuthInfo(user, "google", providerId, email);

        // Cập nhật avatar nếu có
        if (picture != null && !picture.equals(user.getAvatarUrl())) {
            user.setAvatarUrl(picture);
            userRepository.save(user);
        }

        // Tạo CustomUserDetails từ user với attributes
        return new CustomUserDetails(user, attributes);
    }

    private void saveOrUpdateOAuthInfo(User user, String provider, String providerId, String email) {
        Optional<UserOAuth> existingOAuth = userOAuthRepository
                .findByProviderAndProviderId(provider, providerId);

        if (existingOAuth.isPresent()) {
            // Cập nhật thời gian
            UserOAuth oauth = existingOAuth.get();
            oauth.setCreateAt(LocalDateTime.now());
            userOAuthRepository.save(oauth);
        } else {
            // Tạo mới
            UserOAuth userOAuth = new UserOAuth();
            userOAuth.setUser(user);
            userOAuth.setProvider(provider);
            userOAuth.setProviderId(providerId);
            userOAuth.setMailProvider(email);
            userOAuth.setCreateAt(LocalDateTime.now());
            userOAuthRepository.save(userOAuth);
        }
    }

    @Transactional
    public OAuth2Response handleOAuth2Callback(String provider, OAuth2User oauth2User) {
        CustomUserDetails userDetails = (CustomUserDetails) oauth2User;
        User user = userDetails.getUser();

        // Tạo JWT tokens
        String accessToken = jwtTokenProvider.generateOAuth2Token(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshTokenForOAuth(userDetails);

        // Lưu refresh token
        authService.saveRefreshToken(user, refreshToken);

        return OAuth2Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 giờ
                .user(user)
                .isNewUser(isNewUser(user))
                .build();
    }

    private boolean isNewUser(User user) {
        // User được coi là mới nếu tạo trong vòng 5 phút
        return user.getCreateAt().isAfter(LocalDateTime.now().minusMinutes(5));
    }

    // Phương thức để xử lý OAuth2 callback trực tiếp từ controller
    public OAuth2Response processOAuth2PostLogin(OAuth2User oauth2User) {
        CustomUserDetails userDetails = (CustomUserDetails) oauth2User;
        User user = userDetails.getUser();

        // Tạo JWT tokens
        String accessToken = jwtTokenProvider.generateOAuth2Token(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshTokenForOAuth(userDetails);

        // Lưu refresh token
        authService.saveRefreshToken(user, refreshToken);

        return OAuth2Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(user)
                .isNewUser(isNewUser(user))
                .build();
    }
}