package com.example.sd_62.user.repository;

import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    // Tìm user group bằng type
    Optional<UserGroup> findByType(UserGroupType type);

    // Tìm user groups theo danh sách types
    List<UserGroup> findByTypeIn(List<UserGroupType> types);

    // Kiểm tra tên group đã tồn tại chưa
    boolean existsByName(String name);

    // Tìm group theo tên
    Optional<UserGroup> findByName(String name);
}