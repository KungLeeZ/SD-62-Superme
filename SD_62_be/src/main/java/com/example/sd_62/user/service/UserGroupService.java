package com.example.sd_62.user.service;

import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserGroupType;

import java.util.List;

public interface UserGroupService {

    // Tạo user group mới
    UserGroup createUserGroup(UserGroup userGroup);

    // Cập nhật user group
    UserGroup updateUserGroup(Integer id, UserGroup userGroup);

    // Lấy user group by id
    UserGroup getUserGroupById(Integer id);

    // Lấy user group by type
    UserGroup getUserGroupByType(UserGroupType type);

    // Lấy tất cả user groups
    List<UserGroup> getAllUserGroups();

    // Xóa user group
    void deleteUserGroup(Integer id);

    // Kiểm tra group name đã tồn tại
    boolean existsByName(String name);

    // Kiểm tra group type đã tồn tại
    boolean existsByType(UserGroupType type);

    // Lấy các groups mặc định
    List<UserGroup> getDefaultGroups();
}