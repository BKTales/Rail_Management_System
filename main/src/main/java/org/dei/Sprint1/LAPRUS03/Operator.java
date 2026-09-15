package org.dei.Sprint1.LAPRUS03;

public class Operator {
    private String name;
    private String shortName;
    private String vatNumber;

    public Operator(String name, String shortName, String vatNumber) {
        this.name = name;
        this.shortName = shortName;
        this.vatNumber = vatNumber;
    }

    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public String getVatNumber() { return vatNumber; }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, shortName);
    }
}
