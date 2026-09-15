package org.dei.Sprint1.Order;

public enum OrderMode {
    STRICT("STRICT"),
    PARTIAL("PARTIAL");

    private final String name;

    OrderMode(String name) {
        this.name = name;
    }
}
