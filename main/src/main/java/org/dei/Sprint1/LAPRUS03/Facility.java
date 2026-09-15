package org.dei.Sprint1.LAPRUS03;

public class Facility {
    private int id;
    private String name;

    public Facility(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return String.format("%d - %s", id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Facility facility = (Facility) obj;
        return id == facility.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}