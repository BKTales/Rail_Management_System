package org.dei.Sprint1._Item;

public enum Unit {
    PACK("PACK"),
    BOTTLE("BOTTLE"),
    BAG("BAG"),
    UNIT("UNIT"),
    BOX("BOX");

    public final String inString;

    Unit(String inString) {
        this.inString = inString;
    }

    public static Unit fromString(String text){
        for (Unit b : Unit.values()) {
            if (b.inString.equalsIgnoreCase(text)){
                return b;
            }
        }
        return null;
    }
}
