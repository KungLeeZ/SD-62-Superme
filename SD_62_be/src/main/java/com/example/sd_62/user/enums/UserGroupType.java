package com.example.sd_62.user.enums;

public enum UserGroupType {
    ADMIN("Quản trị viên"),
    STAFF("Nhân viên"),
    CUSTOMER("Khách"),
    PREMIUM("Người dùng cao cấp");

    private final String displayName;

    UserGroupType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}