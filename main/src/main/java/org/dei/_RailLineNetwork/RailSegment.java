package org.dei._RailLineNetwork;

import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._Time.CollisionTimes;
import org.dei._Time.TimeInSegment;
import org.dei._Train.Freight;
import org.dei._Train.Train;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RailSegment {
    private final String                        segmentId;
    private float                               gaugeWidth;
    private final double                        distanceKm;
    private final int                           manyTracks;
    private final int                           maxSpeedKmh;
    private final boolean                       isElectrified;
    private final double                        maxWeightPerMeter;
    private Siding                              siding;
    private TreeMap<TimeInSegment, List<Train>> arrivalsAtSegment;
    private static final long                   SAFETY_MARGIN_SECONDS = 30;

    public RailSegment(String segmentId, double distanceKm, int maxSpeedKmh,
                       int manyTracks, boolean isElectrified,
                       double maxWeightPerMeter) {
        this.segmentId = segmentId;
        this.distanceKm = distanceKm;
        this.maxSpeedKmh = maxSpeedKmh;
        this.manyTracks = manyTracks;
        this.isElectrified = isElectrified;
        this.maxWeightPerMeter = maxWeightPerMeter;
        this.siding = null;
    }

    public RailSegment(String segmentId, double distanceKm, int maxSpeedKmh,
                       int manyTracks, boolean isElectrified,
                       double maxWeightPerMeter, int sidingSize, int sidingPosition) {
        this.segmentId = segmentId;
        this.distanceKm = distanceKm;
        this.maxSpeedKmh = maxSpeedKmh;
        this.manyTracks = manyTracks;
        this.isElectrified = isElectrified;
        this.maxWeightPerMeter = maxWeightPerMeter;
        arrivalsAtSegment = new TreeMap<>();
        this.siding = null;
    }

    public RailSegment(String segmentId, double distanceKm, int maxSpeedKmh,
                       int manyTracks, boolean isElectrified,
                       double maxWeightPerMeter, boolean hasSiding, float gaugeWidth) {
        this.segmentId = segmentId;
        this.distanceKm = distanceKm;
        this.maxSpeedKmh = maxSpeedKmh;
        this.manyTracks = manyTracks;
        this.isElectrified = isElectrified;
        this.maxWeightPerMeter = maxWeightPerMeter;
        this.gaugeWidth = gaugeWidth;
        arrivalsAtSegment = new TreeMap<>();
        this.siding = null;
    }

    public RailSegment(String segmentId, double distanceKm, int maxSpeedKmh, int manyTracks, boolean isElectrified,
                       double maxWeightPerMeter, boolean hasSiding, float gauge, int sidingPosition, int sidingSize) {
        this.segmentId = segmentId;
        this.distanceKm = distanceKm;
        this.manyTracks = manyTracks;
        this.maxSpeedKmh = maxSpeedKmh;
        this.isElectrified = isElectrified;
        this.maxWeightPerMeter = maxWeightPerMeter;
        this.arrivalsAtSegment = new TreeMap<>();
        this.gaugeWidth = gauge;
        this.siding = null;
    }


    public boolean canHandleWeight(double trainWeightTons, double trainLengthMeters) {
        double weightPerMeter = (trainWeightTons * 1000) / trainLengthMeters;
        return weightPerMeter <= maxWeightPerMeter;
    }
    public TimeInSegment setArrivalsAtSegment(TimeInSegment timeInSegment, Train train) {
        if (arrivalsAtSegment == null) {
            arrivalsAtSegment = new TreeMap<>();
        }

        //System.out.println("\tarrival at segment time=" + arrival);
        //System.out.println("\tdeparture at segment time=" + departure);
        List<Train> newTrainList = new ArrayList<>();
        newTrainList.add(train);
        setArrivalAtSegmentHelper(timeInSegment, newTrainList);
        return (timeInSegment);
    }


    public TimeInSegment setArrivalsAtSegment(LocalDateTime arrival, Train train) {
        double fullLengthKm = distanceKm + (train.getCurrentLength() / 1000.0);
        double speedKmh = calculateSpeed(train);
        double travelTimeHours = fullLengthKm / speedKmh;
        long travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);
        LocalDateTime departure = arrival.plusNanos(travelTimeNanos);
        TimeInSegment timeInSegment = new TimeInSegment(arrival, departure);
        if (arrivalsAtSegment == null) arrivalsAtSegment = new TreeMap<>();

        List<Train> newTrainList = new ArrayList<>();
        newTrainList.add(train);
        setArrivalAtSegmentHelper(timeInSegment, newTrainList);
        return (timeInSegment);
    }
    
    /**
     * Function will go through each of the entries inside the map trying to find a collision
     * if not found, time will be added straight to the tree, otherwise, the time will be split
     * and will be added the new train in the collision time!
     *
     * @param timeNewTrain
     * @param newTrainList
     */
    public void setArrivalAtSegmentHelper(TimeInSegment timeNewTrain, List<Train> newTrainList) {
        if (timeNewTrain == null) return;
        TimeInSegment collisionTime = isTimeInArrivals(timeNewTrain);

        if (collisionTime == null) {
            arrivalsAtSegment.put(timeNewTrain, newTrainList);
        } else {
            Map.Entry<TimeInSegment, List<Train>> collisionSet = getEntrySet(collisionTime);
            if (collisionSet.getValue().size() < manyTracks) {
                CollisionTimes cTempEntered = timeNewTrain.separateCollisionTime(timeNewTrain, collisionTime);
                CollisionTimes cTempMap = timeNewTrain.separateCollisionTime(collisionSet.getKey(), collisionTime);

                List<Train> originalPlusNewTrain = new ArrayList<>(collisionSet.getValue());
                originalPlusNewTrain.addAll(newTrainList);

                arrivalsAtSegment.remove(collisionSet.getKey());
                arrivalsAtSegment.put(cTempMap.collided, originalPlusNewTrain);

                setArrivalAtSegmentHelper(cTempEntered.before, newTrainList);
                setArrivalAtSegmentHelper(cTempEntered.after, newTrainList);
                setArrivalAtSegmentHelper(cTempMap.before, collisionSet.getValue());
                setArrivalAtSegmentHelper(cTempMap.after, collisionSet.getValue());
            }
            else if (siding != null)
            {
                resolveSidingCollision(timeNewTrain,collisionSet,newTrainList);
            }
            else
                throw new TracksFullException("All tracks are full! Cannot schedule train!");
        }
    }

    private void resolveSidingCollision(TimeInSegment timeNewTrain, Map.Entry<TimeInSegment, List<Train>> collisionSet, List<Train> newTrainList) {
        Train trainMainTrack = collisionSet.getValue().getFirst();
        Train newTrain = newTrainList.getFirst();
        TimeInSegment mainTrackTime = collisionSet.getKey();
        Train mainTrain = mainTrackTime.getTrainById(trainMainTrack.getTrainId());
        LocalDateTime startTime = timeNewTrain.getStartTime();
        double speedMain = this.calculateSpeed(mainTrain);

        //siding calculations
        double speedSiding = this.siding.calculateSpeed(newTrain);
        double distToFrontExit = this.siding.getDistanceKm();
        double distToTailExit = distToFrontExit + (newTrain.getCurrentLength() / 1000.0);
        long durationToTailNanos = (long) ((distToTailExit / speedSiding) * 3_600_000_000_000L);
        LocalDateTime tailAtExitSiding = startTime.plusNanos(durationToTailNanos);

        //Main Track
        double travelTimeHoursMain = this.getDistanceKm() / speedMain;
        long travelTimeNanosMain = (long) (travelTimeHoursMain * 3_600_000_000_000L);
        LocalDateTime mainFrontAtExit = mainTrackTime.getStartTime().plusNanos(travelTimeNanosMain);
        double mainPassageHours = (mainTrain.getCurrentLength() / 1000.0) / speedMain;
        long mainPassageNanos = (long) (mainPassageHours * 3_600_000_000_000L);
        LocalDateTime mainTailAtExit = mainFrontAtExit.plusNanos(mainPassageNanos);

        long myTailPassTimeNanos = (long) (((newTrain.getCurrentLength() / 1000.0) / speedSiding) * 3_600_000_000_000L);
        LocalDateTime finalExit = determineFinalExit(tailAtExitSiding,mainFrontAtExit,mainTailAtExit,myTailPassTimeNanos);

        timeNewTrain.setStartTime(startTime);
        timeNewTrain.setEndTime(finalExit);

        if (this.siding.canAcceptTrain(newTrain, timeNewTrain)) {
            this.siding.setSidingUsage(timeNewTrain, newTrain);
        } else {
            throw new TracksFullException("Siding can´t support this train.");
        }
    }

    private LocalDateTime determineFinalExit(LocalDateTime tailAtExitSiding, LocalDateTime mainFrontAtExit, LocalDateTime mainTailAtExit, long myTailPassTimeNanos) {
        //if tail at siding exit occurres before front of main, then go
        //if not waits until tail of main passes de exit
        if (tailAtExitSiding.isBefore(mainFrontAtExit)) {
           return tailAtExitSiding;
        } else {
            // passage free after main tail passes the end + 30s(secure margin)
            LocalDateTime earliestFrontExit = mainTailAtExit.plusSeconds(SAFETY_MARGIN_SECONDS);

            // exit hour with secure time + length of siding train)
            LocalDateTime safetyTailExit = earliestFrontExit.plusNanos(myTailPassTimeNanos);

            // if siding train was planned to arrive after this time nothing changes
            // if not(conflict), it is delayed to secure time
            if (tailAtExitSiding.isAfter(safetyTailExit)) {
               return tailAtExitSiding;
            } else {
                return safetyTailExit;
            }
        }
    }

    private TimeInSegment isTimeInArrivals(TimeInSegment timeNewTrain){
        if (arrivalsAtSegment == null || timeNewTrain == null) return null;
        for (TimeInSegment timMap : arrivalsAtSegment.keySet()) {
            TimeInSegment t = timMap.hasIntersection(timeNewTrain);
            if (t != null) return t;
        }
        return null;
    }

    private Map.Entry<TimeInSegment, List<Train>> getEntrySet(TimeInSegment collisionTime){
        for (Map.Entry<TimeInSegment, List<Train>> tmpEntry : arrivalsAtSegment.entrySet()) {
            if (tmpEntry.getKey().compareTo(collisionTime) == 0) return tmpEntry;
        }
        return null;
    }

    public List<Train> getTrainListInGivenTime(TimeInSegment time) {
        return (arrivalsAtSegment.get(time));
    }

    public void createSiding(String segmentId, double distanceKm, int maxSpeedKmh, int startPosition) {
        this.siding = new Siding(segmentId, distanceKm, maxSpeedKmh, isElectrified, maxWeightPerMeter, startPosition);
    }

    public double calculateSpeed(Train train) {
        return Math.min(maxSpeedKmh, train.getCurrentMaxSpeed());
    }

    // Getters
    public String getSegmentId() { return segmentId; }
    public double getDistanceKm() { return distanceKm; }
    public int getMaxSpeedKmh() { return maxSpeedKmh; }
    public boolean isElectrified() { return isElectrified; }
    public double getMaxWeightPerMeter() { return maxWeightPerMeter; }
    public TreeMap<TimeInSegment, List<Train>> getArrivalsAtSegment(){ return arrivalsAtSegment; }
    public int getManyTracks() { return manyTracks; }
    public void setSiding(Siding siding) { this.siding = siding; }
    public Siding getSiding() { return (this.siding); }
    public float getGaugeWidth() {
        return gaugeWidth;
    }

    @Override
    public String toString() {
        return "RailSegment{" +
                "hasSiding=" + (siding != null) +
                ", segmentId='" + segmentId + '\'' +
                ", distanceKm=" + distanceKm +
                ", maxSpeedKmh=" + maxSpeedKmh +
                ", isSingleTrack=" + manyTracks +
                ", isElectrified=" + isElectrified +
                ", maxWeightPerMeter=" + maxWeightPerMeter +
                '}';
    }
}