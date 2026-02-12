package com.example.sd_62.user.controller;

import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.enums.UserStatus;
import com.example.sd_62.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users - Lấy tất cả users (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET /api/users/{id} - Lấy user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or @userSecurity.isOwner(#id)")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // POST /api/users - Tạo user mới (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // PUT /api/users/{id} - Cập nhật user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE /api/users/{id} - Xóa user (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/users/search - Tìm kiếm users
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/email/{email} - Lấy user by email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or @userSecurity.isOwnerByEmail(#email)")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // POST /api/users/{id}/change-password - Đổi mật khẩu
    @PostMapping("/{id}/change-password")
    @PreAuthorize("@userSecurity.isOwner(#id)")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    // POST /api/users/{id}/reset-password - Reset mật khẩu (Admin only)
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Integer id,
            @RequestParam String newPassword) {
        User user = userService.getUserById(id);
        userService.resetPassword(user.getEmail(), newPassword);
        return ResponseEntity.ok().build();
    }

    // PUT /api/users/{id}/status - Thay đổi status (Admin only)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeStatus(
            @PathVariable Integer id,
            @RequestParam UserStatus status) {
        User user = userService.changeStatus(id, status);
        return ResponseEntity.ok(user);
    }

    // GET /api/users/group/{groupId} - Lấy users theo group
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<User>> getUsersByGroup(@PathVariable Integer groupId) {
        List<User> users = userService.getUsersByGroup(groupId);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/status/{status} - Lấy users theo status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable UserStatus status) {
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    // POST /api/users/check-email - Kiểm tra email đã tồn tại chưa
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    // POST /api/users/check-phone - Kiểm tra phone đã tồn tại chưa
    @PostMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhone(@RequestParam String phone) {
        boolean exists = userService.phoneExists(phone);
        return ResponseEntity.ok(exists);
    }
}