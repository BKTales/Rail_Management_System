package org.dei.Sprint1.Order;

public enum OrderStatus {
    ELIGIBLE("ELIGIBLE"),
    PARTIAL("PARTIAL"),
    UNDISPATCHABLE("UNDISPATCHABLE");

    private final String name;
    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
