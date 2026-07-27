package com.codesa.user.service.project.enums;

public enum ProjectStatus {
    ACTIVE,
    ARCHIVED,
    CLOSED;

    public static boolean isValid(String status) {
        if (status == null) return false;
        for (ProjectStatus ps : values()) {
            if (ps.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
}
