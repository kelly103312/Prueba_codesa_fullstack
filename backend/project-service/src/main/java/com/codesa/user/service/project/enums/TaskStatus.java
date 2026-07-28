package com.codesa.user.service.project.enums;

public enum TaskStatus {
    CREATED,
    INPROGRESS,
    DONE,
    OVERDUE;

    public static boolean isValid(String status) {
        if (status == null) return false;
        for (TaskStatus ps : values()) {
            if (ps.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
}
