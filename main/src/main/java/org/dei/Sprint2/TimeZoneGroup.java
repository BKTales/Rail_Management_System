package org.dei.Sprint2;

public enum TimeZoneGroup {
    WETGMT(0),
    CET(1),
    EET(2),
    FET(3);

    private int hours;

    TimeZoneGroup(int hours) {
        this.hours = hours;
    }


    public static TimeZoneGroup fromString(String s) {
        if (s == null) return null;
        switch (s.trim().toUpperCase()) {
            case "CET": return CET;
            case "WETGMT":
            case "WET/GMT": return WETGMT;
            case "FET": return FET;
            default: return null;
        }
    }

}
