package com.example.sd_62.user.repository;

import com.example.sd_62.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Tìm user bằng email
    Optional<User> findByEmail(String email);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Tìm user bằng số điện thoại
    Optional<User> findByPhone(String phone);

    // Tìm users theo trạng thái
    List<User> findByStatus(String status);

    // Tìm users theo user group
    List<User> findByGroupId(Integer groupId);

    // Tìm kiếm users theo từ khóa (tên, email, phone)
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phone LIKE CONCAT('%', :keyword, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);

    // Đếm số lượng user theo status
    long countByStatus(String status);
}
