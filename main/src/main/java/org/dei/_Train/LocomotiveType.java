package org.dei._Train;

public enum LocomotiveType {
    ELECTRIC,
    DIESEL;

    public static LocomotiveType fromString(String string) {
        string = string.toLowerCase();
        switch (string) {
            case "electric":
                return ELECTRIC;
            case "diesel":
                return DIESEL;
            default:
                throw new IllegalArgumentException("Unknown LocomotiveType: " + string);
        }

    }
}
