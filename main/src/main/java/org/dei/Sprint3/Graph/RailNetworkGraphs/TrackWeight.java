package org.dei.Sprint3.Graph.RailNetworkGraphs;

import java.util.Comparator;

public class TrackWeight {
    private double cost;
    private double distance;
    private double capacity;

    public TrackWeight(double cost, double distance, double capacity) {
        this.cost = cost;
        this.distance = distance;
        this.capacity = capacity;
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

    /**
     * Comparator for TrackWeight based on distance (km)
     * Used for MST when minimizing total track length
     */
    public static Comparator<TrackWeight> compareByDistance() {
        return Comparator.comparingDouble(TrackWeight::getDistance);
    }

    public int compareTo(TrackWeight o) {
        if (this.cost < o.cost)
            return -1;
        if (this.cost > o.cost)
            return 1;
        return 0;
    }

    public int compareToDistanceOnly(TrackWeight o) {
        if (this.distance < o.distance)
            return -1;
        if (this.distance > o.distance)
            return 1;
        return 0;
    }

    public static TrackWeight apply(TrackWeight trackWeight, TrackWeight trackWeight2) {
        TrackWeight result = new TrackWeight(trackWeight.getCost() + trackWeight2.getCost(),
                            trackWeight.getDistance() + trackWeight2.getDistance(),
                            trackWeight.getCapacity() + trackWeight2.getCapacity());
        return (result);
    }
}
