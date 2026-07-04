package com.training.management.common;

import java.util.Map;
import java.util.Set;

public final class RoleCodes {

    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    public static final String FINANCE_ADMIN = "FINANCE_ADMIN";
    public static final String ENGINEER_LEAD = "ENGINEER_LEAD";

    private static final Map<String, Set<String>> ROLE_MODULES = Map.of(
        SUPER_ADMIN, Set.of(
            "dashboard", "communities", "residents", "properties", "repairs", "complaints",
            "billing", "parking", "notices", "smart-services", "statistics", "security", "system-users"
        ),
        SERVICE_MANAGER, Set.of(
            "dashboard", "communities", "residents", "properties", "repairs", "complaints",
            "parking", "notices", "smart-services", "statistics", "security"
        ),
        FINANCE_ADMIN, Set.of("dashboard", "billing", "parking", "residents", "statistics"),
        ENGINEER_LEAD, Set.of("dashboard", "communities", "properties", "repairs", "parking", "statistics", "security")
    );

    private RoleCodes() {
    }

    public static boolean hasAnyRole(String currentRole, String[] allowedRoles) {
        if (SUPER_ADMIN.equals(currentRole)) {
            return true;
        }
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(currentRole)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAccessModule(String currentRole, String module) {
        return ROLE_MODULES.getOrDefault(currentRole, Set.of()).contains(module);
    }
}
