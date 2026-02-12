package com.example.sd_62.user.controller;

import com.example.sd_62.user.entity.UserGroup;
import com.example.sd_62.user.enums.UserGroupType;
import com.example.sd_62.user.service.UserGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserGroupController {
    
    private final UserGroupService userGroupService;
    
    @GetMapping
    public ResponseEntity<List<UserGroup>> getAllUserGroups() {
        List<UserGroup> groups = userGroupService.getAllUserGroups();
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> getUserGroupById(@PathVariable Integer id) {
        UserGroup group = userGroupService.getUserGroupById(id);
        return ResponseEntity.ok(group);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<UserGroup> getUserGroupByType(@PathVariable UserGroupType type) {
        UserGroup group = userGroupService.getUserGroupByType(type);
        return ResponseEntity.ok(group);
    }
    
    @PostMapping
    public ResponseEntity<UserGroup> createUserGroup(@Valid @RequestBody UserGroup userGroup) {
        UserGroup createdGroup = userGroupService.createUserGroup(userGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> updateUserGroup(
            @PathVariable Integer id,
            @Valid @RequestBody UserGroup userGroup) {
        UserGroup updatedGroup = userGroupService.updateUserGroup(id, userGroup);
        return ResponseEntity.ok(updatedGroup);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Integer id) {
        userGroupService.deleteUserGroup(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/default")
    public ResponseEntity<List<UserGroup>> getDefaultGroups() {
        List<UserGroup> groups = userGroupService.getDefaultGroups();
        return ResponseEntity.ok(groups);
    }
    
    @PostMapping("/check-name")
    public ResponseEntity<Boolean> checkNameExists(@RequestParam String name) {
        boolean exists = userGroupService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping("/check-type")
    public ResponseEntity<Boolean> checkTypeExists(@RequestParam UserGroupType type) {
        boolean exists = userGroupService.existsByType(type);
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping("/initialize-defaults")
    public ResponseEntity<Void> initializeDefaultGroups() {
        // Nếu có phương thức initialize trong service
        // userGroupService.initializeDefaultGroups();
        return ResponseEntity.ok().build();
    }
}