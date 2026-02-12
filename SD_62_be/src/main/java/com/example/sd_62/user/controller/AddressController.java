package com.example.sd_62.user.controller;

import com.example.sd_62.user.entity.Address;
import com.example.sd_62.user.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    
    private final AddressService addressService;
    
    // GET /api/addresses/user/{userId} - Lấy addresses của user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or @userSecurity.isOwner(#userId)")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Integer userId) {
        List<Address> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }
    
    // GET /api/addresses/{id} - Lấy address by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or @addressSecurity.isOwner(#id)")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
    
    // POST /api/addresses/user/{userId} - Thêm address mới
    @PostMapping("/user/{userId}")
    @PreAuthorize("@userSecurity.isOwner(#userId)")
    public ResponseEntity<Address> addAddress(
            @PathVariable Integer userId,
            @Valid @RequestBody Address address) {
        Address createdAddress = addressService.addAddress(userId, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }
    
    // PUT /api/addresses/{id} - Cập nhật address
    @PutMapping("/{id}")
    @PreAuthorize("@addressSecurity.isOwner(#id)")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Integer id,
            @Valid @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, address);
        return ResponseEntity.ok(updatedAddress);
    }
    
    // DELETE /api/addresses/{id} - Xóa address
    @DeleteMapping("/{id}")
    @PreAuthorize("@addressSecurity.isOwner(#id)")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
    
    // GET /api/addresses/user/{userId}/default - Lấy address mặc định
    @GetMapping("/user/{userId}/default")
    @PreAuthorize("@userSecurity.isOwner(#userId)")
    public ResponseEntity<Address> getDefaultAddress(@PathVariable Integer userId) {
        try {
            Address address = addressService.getDefaultAddress(userId);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT /api/addresses/{id}/set-default - Đặt address làm mặc định
    @PutMapping("/{id}/set-default")
    @PreAuthorize("@addressSecurity.isOwner(#id)")
    public ResponseEntity<Address> setDefaultAddress(@PathVariable Integer id) {
        Address address = addressService.setDefaultAddress(id);
        return ResponseEntity.ok(address);
    }
    
    // GET /api/addresses/user/{userId}/count - Đếm số addresses
    @GetMapping("/user/{userId}/count")
    @PreAuthorize("@userSecurity.isOwner(#userId)")
    public ResponseEntity<Long> countUserAddresses(@PathVariable Integer userId) {
        long count = addressService.countUserAddresses(userId);
        return ResponseEntity.ok(count);
    }
    
    // GET /api/addresses/user/{userId}/has-default - Kiểm tra có address mặc định chưa
    @GetMapping("/user/{userId}/has-default")
    @PreAuthorize("@userSecurity.isOwner(#userId)")
    public ResponseEntity<Boolean> hasDefaultAddress(@PathVariable Integer userId) {
        boolean hasDefault = addressService.hasDefaultAddress(userId);
        return ResponseEntity.ok(hasDefault);
    }
}