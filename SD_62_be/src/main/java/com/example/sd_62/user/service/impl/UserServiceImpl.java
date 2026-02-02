package com.example.sd_62.user.service.impl;

import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserGroupType;
import com.example.sd_62.user.enums.UserStatus;
import com.example.sd_62.user.repository.UserGroupRepository;
import com.example.sd_62.user.repository.UserRepository;
import com.example.sd_62.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());

        // Kiểm tra email đã tồn tại
        if (emailExists(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + user.getEmail());
        }

        // Kiểm tra phone nếu có
        if (user.getPhone() != null && phoneExists(user.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + user.getPhone());
        }

        // Mã hóa password
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Set timestamps
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        // Set default status nếu chưa có
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        // Set default group nếu chưa có
        if (user.getGroup() == null) {
            UserGroup defaultGroup = userGroupRepository.findByType(UserGroupType.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng mặc định"));
            user.setGroup(defaultGroup);
        }

        User savedUser = userRepository.save(user);
        log.info("Created user successfully: {}", savedUser.getEmail());

        return savedUser;
    }

    @Override
    public User updateUser(Integer id, User userDetails) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        // Cập nhật name
        if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
            user.setName(userDetails.getName());
        }

        // Cập nhật email (nếu thay đổi)
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (emailExists(userDetails.getEmail())) {
                throw new RuntimeException("Email đã tồn tại: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        // Cập nhật phone (nếu thay đổi)
        if (userDetails.getPhone() != null && !userDetails.getPhone().equals(user.getPhone())) {
            if (phoneExists(userDetails.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại: " + userDetails.getPhone());
            }
            user.setPhone(userDetails.getPhone());
        }

        // Cập nhật group
        if (userDetails.getGroup() != null) {
            user.setGroup(userDetails.getGroup());
        }

        // Cập nhật status
        if (userDetails.getStatus() != null) {
            user.setStatus(userDetails.getStatus());
        }

        // Cập nhật password (nếu có)
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Cập nhật avatar (nếu có)
        if (userDetails.getAvatarUrl() != null) {
            user.setAvatarUrl(userDetails.getAvatarUrl());
        }

        user.setUpdateAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Updated user successfully: {}", updatedUser.getEmail());

        return updatedUser;
    }

    @Override
    public User getUserById(Integer id) {
        log.debug("Getting user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        log.debug("Getting user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);
        User user = getUserById(id);
        user.setStatus(UserStatus.INACTIVE);
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Deleted user successfully: {}", user.getEmail());
    }

    @Override
    public List<User> searchUsers(String keyword) {
        log.debug("Searching users with keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.searchUsers(keyword.trim());
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        log.info("Changing password for user id: {}", userId);

        User user = getUserById(userId);

        // Kiểm tra old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        // Cập nhật new password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Changed password successfully for user: {}", user.getEmail());
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        log.info("Resetting password for email: {}", email);

        User user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Reset password successfully for user: {}", email);
    }

    @Override
    public User changeStatus(Integer userId, UserStatus status) {
        log.info("Changing status to {} for user id: {}", status, userId);

        User user = getUserById(userId);
        user.setStatus(status);
        user.setUpdateAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Changed status successfully for user: {}", updatedUser.getEmail());

        return updatedUser;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean phoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getActiveUsersCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE.name());
    }

    @Override
    public List<User> getUsersByGroup(Integer groupId) {
        return userRepository.findByGroupId(groupId);
    }

    @Override
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status.name());
    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }
}