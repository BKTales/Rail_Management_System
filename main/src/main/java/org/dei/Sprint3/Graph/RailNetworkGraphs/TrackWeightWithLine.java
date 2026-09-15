package org.dei.Sprint3.Graph.RailNetworkGraphs;

import org.dei._RailLineNetwork.RailLine;

public class TrackWeightWithLine {
    private double cost;
    private double distance;
    private double capacity;
    private RailLine line;

    public TrackWeightWithLine(RailLine line) {
        this.line = line;
        this.distance = line.getTotalDistance();
        this.capacity = line.getMediumCapacity();
        this.cost = line.getTotalDistance() * 0.8 + capacity * 0.2; // ask lapr teacher about the cost!
    }

    // only to be used in dijkstra !
    public TrackWeightWithLine(double cost, double distance, double capacity) {
        this.line = null;
        this.distance = distance;
        this.capacity = capacity;
        this.cost = distance * 0.8 + capacity * 0.2; // ask lapr teacher about the cost!
    }

    public RailLine getLine() {
        return line;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public int compareTo(TrackWeightWithLine o) {
        if (this.cost < o.cost)
            return -1;
        if (this.cost > o.cost)
            return 1;
        return 0;
    }

    public int compareToDistanceOnly(TrackWeightWithLine o) {
        if (this.distance < o.distance)
            return -1;
        if (this.distance > o.distance)
            return 1;
        return 0;
    }

    public static TrackWeightWithLine apply(TrackWeightWithLine trackWeight, TrackWeightWithLine trackWeight2) {
        TrackWeightWithLine result = new TrackWeightWithLine(trackWeight.getCost() + trackWeight2.getCost(),
                trackWeight.getDistance() + trackWeight2.getDistance(),
                trackWeight.getCapacity() + trackWeight2.getCapacity());
        return (result);
    }
}
