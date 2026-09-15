package org.dei.Authentication;

public enum UserRole2 {
    TRAIN_DRIVER("TRAIN_DRIVER", "Train Driver role"),
    STATION_MASTER("STATION_MASTER", "Station Master role"),
    STATION_STORAGE_MANAGER("STATION_STORAGE_MANAGER", "Station Storage Manager role"),
    FREIGHT_MANAGER("FREIGHT_MANAGER", "Freight Manager role"),
    PLANNER("PLANNER", "Planner role");

    UserRole2(String roleId, String roleDescription) {
        this.roleId = roleId;
        this.roleDescription = roleDescription;
    }

    public String roleId;
    public String roleDescription;
}
