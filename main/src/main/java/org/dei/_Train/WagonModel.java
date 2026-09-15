package org.dei._Train;

public class WagonModel {
    private final String id;
    private final double length; // m
    private final double height;
    private final double width;
    private final double weight; //T
    private final double maxSpeed;
    private final String gauge;
    private final int boxCapacity;

    public WagonModel(String id, double length, double height, double width, double weight, double maxSpeed, String gauge, int boxCapacity) {
        this.id = id;
        this.length = length / 1000;
        this.height = height;
        this.width = width;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.gauge = gauge;
        this.boxCapacity = boxCapacity;
    }

    public String getId()       { return id; }
    public double getLength()   { return length; }
    public double getHeight()   { return height; }
    public double getWidth()    { return width; }
    public double getWeight()   { return weight; }
    public double getMaxSpeed() { return maxSpeed; }
    public String getGauge()    { return gauge; }
    public int getBoxCapacity() { return boxCapacity; }
}