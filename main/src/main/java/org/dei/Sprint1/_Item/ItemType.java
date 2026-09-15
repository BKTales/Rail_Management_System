package org.dei.Sprint1._Item;

public enum ItemType {
    CLEANING("CLEANING"),
    ELECTRONICS("ELECTRONICS"),
    HARDWARE("HARDWARE"),
    BEVERAGE("BEVERAGE"),
    PERSONAL_CARE("PERSONALCARE"),
    GROCERY("GROCERY");

    public final String inString;

    ItemType(String inString) {
        this.inString = inString;
    }

    public static ItemType fromString(String text){
        for (ItemType b : ItemType.values()) {
            if (b.inString.equalsIgnoreCase(text)){
                return b;
            }
        }
        return null;
    }
}
