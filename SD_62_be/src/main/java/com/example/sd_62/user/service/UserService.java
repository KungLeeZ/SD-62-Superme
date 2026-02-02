package com.example.sd_62.user.service;

import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.enums.UserStatus;

import java.util.List;

public interface UserService {

    // Tạo user mới
    User createUser(User user);

    // Cập nhật user
    User updateUser(Integer id, User user);

    // Lấy user by id
    User getUserById(Integer id);

    // Lấy user by email
    User getUserByEmail(String email);

    // Lấy tất cả users
    List<User> getAllUsers();

    // Xóa user (soft delete)
    void deleteUser(Integer id);

    // Tìm kiếm users
    List<User> searchUsers(String keyword);

    // Thay đổi password
    void changePassword(Integer userId, String oldPassword, String newPassword);

    // Reset password
    void resetPassword(String email, String newPassword);

    // Thay đổi status
    User changeStatus(Integer userId, UserStatus status);

    // Kiểm tra email đã tồn tại
    boolean emailExists(String email);

    // Kiểm tra phone đã tồn tại
    boolean phoneExists(String phone);

    // Thống kê
    long getTotalUsers();

    long getActiveUsersCount();

    // Lấy users theo group
    List<User> getUsersByGroup(Integer groupId);

    // Lấy users theo status
    List<User> getUsersByStatus(UserStatus status);

    // Kiểm tra user có tồn tại không
    boolean existsById(Integer id);
}