package com.example.sd_62.configuration.security;

import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.enums.UserStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final User user;
    private Map<String, Object> attributes;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Dựa vào type của UserGroup để phân quyền
        String role = "ROLE_" + user.getGroup().getType().name();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getStatus().equals(UserStatus.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(UserStatus.ACTIVE);
    }

    // OAuth2User methods
    @Override
    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            // Return basic attributes if not set
            return Map.of(
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "id", user.getId()
            );
        }
        return attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    public Integer getUserId() {
        return user.getId();
    }

    public UserStatus getUserStatus() {
        return user.getStatus();
    }
}