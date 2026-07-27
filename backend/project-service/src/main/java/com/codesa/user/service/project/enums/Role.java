package com.codesa.user.service.project.enums;

public enum Role {
    ADMIN(true, true, true, true),
    USER(false, true, false, false);

    private final boolean canEdit;
    private final boolean canCreate;
    private final boolean canDelete;
    private final boolean canArchive;

    Role(boolean canEdit, boolean canCreate, boolean canDelete, boolean canArchive) {
        this.canEdit = canEdit;
        this.canCreate = canCreate;
        this.canDelete = canDelete;
        this.canArchive = canArchive;
    }

    public boolean canEdit() { return canEdit; }
    public boolean canCreate() { return canCreate; }
    public boolean canDelete() { return canDelete; }
    public boolean canArchive() { return canArchive; }

    public static Role fromString(String role) {
        for (Role r : values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        return USER;
    }
}
