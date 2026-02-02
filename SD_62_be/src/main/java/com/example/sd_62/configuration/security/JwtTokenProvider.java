package com.example.sd_62.configuration.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;

    // Không cần @Value cho oauthExpiration, dùng default
    private long oauthExpiration = 3600000; // Default 1 hour

    // Tạo access token từ Authentication
    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getUser().getId())
                .claim("authorities", authorities)
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Tạo access token từ OAuth2User
    public String generateOAuth2Token(OAuth2User oauth2User) {
        CustomUserDetails userDetails = (CustomUserDetails) oauth2User;

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getUser().getId())
                .claim("authorities", authorities)
                .claim("type", "OAUTH_ACCESS")
                .claim("provider", "google")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + oauthExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Tạo refresh token từ Authentication
    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getUser().getId())
                .claim("type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Tạo refresh token từ OAuth2User
    public String generateRefreshTokenForOAuth(OAuth2User oauth2User) {
        CustomUserDetails userDetails = (CustomUserDetails) oauth2User;

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getUser().getId())
                .claim("type", "REFRESH")
                .claim("provider", "google")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy username từ token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Lấy ID từ token
    public Integer getUserIdFromToken(String token) {
        return getClaims(token).get("id", Integer.class);
    }

    // Kiểm tra loại token
    public String getTokenType(String token) {
        return getClaims(token).get("type", String.class);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Token JWT không hợp lệ");
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT đã hết hạn");
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT không được hỗ trợ");
        } catch (IllegalArgumentException ex) {
            log.error("Chuỗi claims JWT trống");
        }
        return false;
    }

    // Validate refresh token
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            String type = claims.get("type", String.class);
            return "REFRESH".equals(type) && validateToken(token);
        } catch (Exception ex) {
            log.error("Refresh token không hợp lệ", ex);
            return false;
        }
    }

    // Lấy claims từ token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Tạo signing key
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Get expiration time
    public Long getExpirationTime() {
        return jwtExpiration / 1000; // Convert to seconds
    }
}