package com.gaoshin.beans;

public enum UserRole {
    SUPER(0),
    ADMIN(20),
    TEST(30),
    USER(50),
    GUEST(100),
    BADGUY(200),
    DELETED(300);

    private int permission;

    private UserRole(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }
}
