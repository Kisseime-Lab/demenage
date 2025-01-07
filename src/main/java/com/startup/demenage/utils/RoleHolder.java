package com.startup.demenage.utils;

import org.springframework.stereotype.Component;

@Component
public class RoleHolder {

    private static final ThreadLocal<String> roleThreadLocal = new ThreadLocal<>();

    public void setRole(String role) {
        roleThreadLocal.set(role);
    }

    public String getRole() {
        return roleThreadLocal.get();
    }

    public void clearRole() {
        roleThreadLocal.remove();
    }
}

