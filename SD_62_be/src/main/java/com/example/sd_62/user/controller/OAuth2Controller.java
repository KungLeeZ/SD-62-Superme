package com.example.sd_62.user.controller;

import com.example.sd_62.user.dto.response.OAuth2Response;
import com.example.sd_62.user.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {
    
    private final OAuth2Service oauth2Service;
    
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;
    
    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;
    
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri:}")
    private String googleRedirectUri;
    
    @GetMapping("/google/url")
    public ResponseEntity<?> getGoogleAuthUrl() {
        if (googleClientId.isEmpty()) {
            return ResponseEntity.badRequest().body("Google OAuth2 chưa được cấu hình");
        }
        
        String googleAuthUrl = UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "profile email")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
        
        return ResponseEntity.ok().body(java.util.Map.of("url", googleAuthUrl));
    }
    
    @GetMapping("/success")
    public void oauth2Success(
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletResponse response) throws IOException {
        
        try {
            OAuth2Response oauthResponse = oauth2Service.processOAuth2PostLogin(oauth2User);
            
            // Redirect về frontend với token
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendUrl + "/oauth2/redirect")
                    .queryParam("access_token", oauthResponse.getAccessToken())
                    .queryParam("refresh_token", oauthResponse.getRefreshToken())
                    .queryParam("is_new_user", oauthResponse.isNewUser())
                    .queryParam("user_email", URLEncoder.encode(
                            oauthResponse.getUser().getEmail(), 
                            StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
            
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            String errorUrl = frontendUrl + "/oauth2/redirect?error=" + 
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
    }
    
    @GetMapping("/failure")
    public void oauth2Failure(HttpServletResponse response) throws IOException {
        String errorUrl = frontendUrl + "/oauth2/redirect?error=Authentication failed";
        response.sendRedirect(errorUrl);
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> testOAuth2() {
        return ResponseEntity.ok("OAuth2 endpoint is working");
    }
}