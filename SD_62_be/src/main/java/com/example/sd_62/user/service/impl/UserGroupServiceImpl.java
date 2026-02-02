package com.example.sd_62.user.service.impl;

import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserGroupType;
import com.example.sd_62.user.repository.UserGroupRepository;
import com.example.sd_62.user.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Override
    public UserGroup createUserGroup(UserGroup userGroup) {
        log.info("Creating new user group: {}", userGroup.getName());

        // Kiểm tra name đã tồn tại
        if (existsByName(userGroup.getName())) {
            throw new RuntimeException("Tên nhóm đã tồn tại: " + userGroup.getName());
        }

        // Kiểm tra type đã tồn tại
        if (userGroup.getType() != null && existsByType(userGroup.getType())) {
            throw new RuntimeException("Loại nhóm đã tồn tại: " + userGroup.getType());
        }

        UserGroup savedGroup = userGroupRepository.save(userGroup);
        log.info("Created user group successfully: {}", savedGroup.getName());

        return savedGroup;
    }

    @Override
    public UserGroup updateUserGroup(Integer id, UserGroup userGroupDetails) {
        log.info("Updating user group with id: {}", id);

        UserGroup userGroup = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm với ID: " + id));

        // Cập nhật name nếu thay đổi
        if (userGroupDetails.getName() != null && !userGroupDetails.getName().equals(userGroup.getName())) {
            if (existsByName(userGroupDetails.getName())) {
                throw new RuntimeException("Tên nhóm đã tồn tại: " + userGroupDetails.getName());
            }
            userGroup.setName(userGroupDetails.getName());
        }

        // Type không thể thay đổi
        if (userGroupDetails.getType() != null && userGroupDetails.getType() != userGroup.getType()) {
            throw new RuntimeException("Không thể thay đổi loại nhóm");
        }

        // Cập nhật description
        if (userGroupDetails.getDescription() != null) {
            userGroup.setDescription(userGroupDetails.getDescription());
        }

        UserGroup updatedGroup = userGroupRepository.save(userGroup);
        log.info("Updated user group successfully: {}", updatedGroup.getName());

        return updatedGroup;
    }

    @Override
    public UserGroup getUserGroupById(Integer id) {
        return userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm với ID: " + id));
    }

    @Override
    public UserGroup getUserGroupByType(UserGroupType type) {
        return userGroupRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm với loại: " + type));
    }

    @Override
    public List<UserGroup> getAllUserGroups() {
        return userGroupRepository.findAll();
    }

    @Override
    public void deleteUserGroup(Integer id) {
        log.info("Deleting user group with id: {}", id);

        UserGroup group = getUserGroupById(id);

        // TODO: Kiểm tra xem group có đang được sử dụng không
        // Có thể thêm logic kiểm tra user có đang sử dụng group này không

        userGroupRepository.delete(group);
        log.info("Deleted user group successfully: {}", group.getName());
    }

    @Override
    public boolean existsByName(String name) {
        return userGroupRepository.existsByName(name);
    }

    @Override
    public boolean existsByType(UserGroupType type) {
        return userGroupRepository.findByType(type).isPresent();
    }

    @Override
    public List<UserGroup> getDefaultGroups() {
        return userGroupRepository.findByTypeIn(List.of(
                UserGroupType.ADMIN,
                UserGroupType.STAFF,
                UserGroupType.CUSTOMER,
                UserGroupType.PREMIUM
        ));
    }

    // Phương thức helper để tạo groups mặc định nếu chưa có
    public void initializeDefaultGroups() {
        for (UserGroupType type : UserGroupType.values()) {
            if (!existsByType(type)) {
                UserGroup group = new UserGroup();
                group.setName(type.getDisplayName());
                group.setType(type);
                group.setDescription("Nhóm " + type.getDisplayName());

                createUserGroup(group);
                log.info("Created default group: {}", type.name());
            }
        }
    }
}