package com.example.sd_62.configuration.security;

import com.example.sd_62.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("addressSecurity")
@RequiredArgsConstructor
public class AddressSecurity {
    
    private final AddressService addressService;
    private final UserSecurity userSecurity;
    
    // Kiểm tra user hiện tại có phải là owner của address không
    public boolean isOwner(Integer addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Integer userId = userDetails.getUserId();
            
            return addressService.belongsToUser(addressId, userId);
        }
        
        return false;
    }
}