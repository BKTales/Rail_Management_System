package org.dei.Sprint1._Item;

public enum Reason {
    CYCLE_COUNT("CYCLE_COUNT"),
    CUSTOMER_REMORSE("CUSTOMER_REMORSE"),
    EXPIRED("EXPIRED"),
    DAMAGED("DAMAGED"),
    WRONG_ITEM("WRONG_ITEM");

    public final String inString;

    Reason(String inString) {
        this.inString = inString;
    }

    public static Reason fromString(String text) {
        if (text == null) return null;
        text = text.trim().replace("-", "_").toUpperCase();
        for (Reason b : Reason.values()) {
            if (b.inString.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

