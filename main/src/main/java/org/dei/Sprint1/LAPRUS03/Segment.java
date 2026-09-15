package org.dei.Sprint1.LAPRUS03;

public class Segment {
    private int id;
    private int lineId;
    private int order;
    private boolean electrified;
    private double maxWeight; // kg/m
    private double length; // meters
    private int numberTracks;

    public Segment(int id, int lineId, int order, boolean electrified, double maxWeight, double length, int numberTracks) {
        this.id = id;
        this.lineId = lineId;
        this.order = order;
        this.electrified = electrified;
        this.maxWeight = maxWeight;
        this.length = length;
        this.numberTracks = numberTracks;
    }

    public int getId() { return id; }
    public int getLineId() { return lineId; }
    public int getOrder() { return order; }
    public boolean isElectrified() { return electrified; }
    public double getMaxWeight() { return maxWeight; }
    public double getLength() { return length; }
    public int getNumberTracks() { return numberTracks; }

    @Override
    public String toString() {
        return String.format("Segment %d (Line %d, Order %d):%.1fm", id, lineId, order, length);
    }
}
