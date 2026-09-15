package org.dei._RailLineNetwork;

import org.dei._Time.TimeInSegment;
import org.dei._Train.Train;

import java.util.List;
import java.util.TreeMap;

/**
 * Represents a segment of track between two stations
 */
public class Siding {
    private final double    maxWeightPerMeter; // kg/m
    private final boolean   isElectrified;
    private int             startPosition;
    private final int       maxSpeedKmh;
    private float           gaugeWidth;
    private final double    distanceKm;
    private final String    sidingId;

    private TreeMap<TimeInSegment, Train> arrivalAtSiding; // this will be used to check if there are any trains
    // unsing tre map because it uses a comparator to check keys! // passing by it at a givenTime

    public Siding(String segmentId, double distanceKm, int maxSpeedKmh, boolean isElectrified,
                  double maxWeightPerMeter, int startPosition) {
        this.sidingId = segmentId;
        this.distanceKm = distanceKm;
        this.maxSpeedKmh = maxSpeedKmh;
        this.isElectrified = isElectrified;
        this.startPosition = startPosition;
        this.maxWeightPerMeter = maxWeightPerMeter;
        arrivalAtSiding = new TreeMap<>();
    }


    /**
     * Check if this segment can handle given train weight
     */
    public boolean canHandleWeight(double trainWeightTons, double trainLengthMeters) {
        double weightPerMeter = (trainWeightTons * 1000) / trainLengthMeters;
        return weightPerMeter <= maxWeightPerMeter;
    }

    public boolean canFitTrain(Train train) {
        double sidingLengthMeters = distanceKm * 1000;
        return train.getCurrentLength() <= sidingLengthMeters;
    }

    public boolean canAcceptTrain(Train train, TimeInSegment time) {
        if (!canFitTrain(train)) return false;
        if (!canHandleWeight(train.getCurrentWeight(),
                train.getCurrentLength()))
            return false;
        if (isTimeInArrivals(time) != null)
            return false;

        return true;
    }

    public double calculateSpeed(Train train) {
        return Math.min(maxSpeedKmh,train.getMaxSpeed());
    }

    public void setSidingUsage(TimeInSegment timeInSegment, Train train) {
        if (isTimeInArrivals(timeInSegment) != null)
            throw new TracksFullException("Siding is full at wanted time!");
        arrivalAtSiding.put(timeInSegment, train);
    }

    private TimeInSegment isTimeInArrivals(TimeInSegment timeNewTrain){
        if (arrivalAtSiding.keySet() == null || timeNewTrain == null)
            return null;
        for (TimeInSegment timMap : arrivalAtSiding.keySet())
        {
            TimeInSegment t = timMap.hasIntersection(timeNewTrain);
            if (t != null)
                return (t);
        }
        return (null);
    }

    private void copyList(List<Train> from, List<Train> to){
        for (Train fromTrain : from) {
            to.add(fromTrain);
        }
    }

    // Getters
    public String getSegmentId() { return sidingId; }
    public double getDistanceKm() { return distanceKm; }
    public int getMaxSpeedKmh() { return maxSpeedKmh; }
    public boolean isElectrified() { return isElectrified; }
    public double getMaxWeightPerMeter() { return maxWeightPerMeter; }
    public TreeMap<TimeInSegment, Train> getArrivalAtSiding(){ return arrivalAtSiding; }
    public float getGaugeWidth() {
        return gaugeWidth;
    }
    public void removeSidingUsage(TimeInSegment time) {
        arrivalAtSiding.remove(time);
    }

    @Override
    public String toString() {
        return "Siding{" +
                ", SidingId='" + sidingId + '\'' +
                ", distanceKm=" + distanceKm +
                ", maxSpeedKmh=" + maxSpeedKmh +
                ", isElectrified=" + isElectrified +
                ", maxWeightPerMeter=" + maxWeightPerMeter +
                '}';
    }
}