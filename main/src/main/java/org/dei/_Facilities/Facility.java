package org.dei._Facilities;

import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Path.Path;
import org.dei._RailLineNetwork.*;
import org.dei._Time.CollisionTimes;
import org.dei._Time.TimeInSegment;
import org.dei._Train.Train;

import java.time.LocalDateTime;
import java.util.*;

public class Facility {
    private final int MAX_TRAIN_IN_FACILITY_AT_SAME_TIME = 10;
    // this hash map holds <nextFacility, RailLine to it> just to make searches faster O(1)!
    protected TreeMap<TimeInSegment, List<Train>>   trainInFacilityByTime;
    protected final TimeZoneGroup                   timeZoneGroup;
    protected HashMap<Facility, RailLine>           connections;
    protected GeographicalLocation                  location;
    protected final TimeZone                        timeZone;
    protected final Country                         country;
    protected final String                          name;
    protected final int                             id;

    public Facility(GeographicalLocation location, TimeZoneGroup timeZoneGroup, TimeZone timeZone, Country country, String name, int id) {
        this.location = location;
        this.timeZoneGroup = timeZoneGroup;
        this.timeZone = timeZone;
        this.country = country;
        this.name = name;
        this.id = id;
        this.connections = new HashMap<Facility, RailLine>();
        this.trainInFacilityByTime = new TreeMap<TimeInSegment, List<Train>>();
    }

    // don't erase this constructor otherwise the tests from sprint 1 don't compile because they are outdated and need an empty constructor
    public Facility() {
        this.location = new GeographicalLocation(-1, -1);
        this.timeZoneGroup = TimeZoneGroup.CET;
        this.timeZone = new TimeZone("unitarytest");
        this.country = new Country("unitarytest");
        this.name = "unitarytest";
        this.id = -1;
        this.connections = new HashMap<Facility, RailLine>();
        this.trainInFacilityByTime = new TreeMap<TimeInSegment, List<Train>>();
    }

    public Facility(int id, String name) { //This is used by the parser
        this.location = new GeographicalLocation(-1, -1);
        this.timeZoneGroup = TimeZoneGroup.CET;
        this.timeZone = new TimeZone("PARSED");
        this.country = new Country("PARSED");
        this.name = name;
        this.id = id;
        this.connections = new HashMap<Facility, RailLine>();
        this.trainInFacilityByTime = new TreeMap<TimeInSegment, List<Train>>();
    }

    public Facility(int id, String name, int index) { //This is used by the parser
        this.location = new GeographicalLocation(index, index);
        this.timeZoneGroup = TimeZoneGroup.CET;
        this.timeZone = new TimeZone("PARSED");
        this.country = new Country("PARSED");
        this.name = name;
        this.id = id;
        this.connections = new HashMap<Facility, RailLine>();
        this.trainInFacilityByTime = new TreeMap<TimeInSegment, List<Train>>();
    }

    public Facility(int id, String name, float latitude,float longitude) { //This is used by the parser
        this.location = new GeographicalLocation(latitude, longitude);
        this.timeZoneGroup = TimeZoneGroup.CET;
        this.timeZone = new TimeZone("PARSED");
        this.country = new Country("PARSED");
        this.name = name;
        this.id = id;
        this.connections = new HashMap<Facility, RailLine>();
        this.trainInFacilityByTime = new TreeMap<TimeInSegment, List<Train>>();
    }

    public LocalDateTime getTrainsByTime(Train train){
        for(TimeInSegment l: trainInFacilityByTime.keySet()){
            if(trainInFacilityByTime.get(l).equals(train)){
                return l.getStartTime(); // in the future return Start and End if Start != End

            }
        }
        return null;
    }

    public HashMap<Facility, RailLine> getConnections() {
        return connections;
    }

    public void createConnection(Facility facility, RailLine railLine){
        connections.put(facility, railLine);
    }

    public void addTrainInFacility(Train train, LocalDateTime start,  LocalDateTime end) {
        TimeInSegment timeInFacility = new TimeInSegment(start, end);
        List<Train> newTrainList = new ArrayList<>();
        newTrainList.add(train);
        setArrivalAtSegmentHelper(timeInFacility, newTrainList);
    }

    public void setArrivalAtSegmentHelper(TimeInSegment timeInFacility, List<Train> newTrainList) {
        if (timeInFacility == null)
            return;

        TimeInSegment collisionTime = isTimeInArrivals(timeInFacility);

        if (collisionTime == null)
            trainInFacilityByTime.put(timeInFacility, newTrainList);
        else
        {
            Map.Entry<TimeInSegment, List<Train>> collisionSet = getEntrySet(collisionTime);
            if (collisionSet.getValue().size() < MAX_TRAIN_IN_FACILITY_AT_SAME_TIME)
            {
                CollisionTimes cTempEntered = timeInFacility
                        .separateCollisionTime(timeInFacility
                                , collisionTime);
                CollisionTimes cTempMap = timeInFacility
                        .separateCollisionTime(collisionSet.getKey(), collisionTime);
                // List<Train> listTrainEntered is newTrainList;
                // List<Train> listTrainInMap is collisionSet.getValue();
                List<Train> originalPlusNewTrain = new ArrayList<>(collisionSet.getValue());
                copyList(newTrainList, originalPlusNewTrain);

                // collision!
                trainInFacilityByTime.remove(collisionSet.getKey());
                trainInFacilityByTime.put(cTempMap.collided, originalPlusNewTrain);

                // send back others!
                setArrivalAtSegmentHelper(cTempEntered.before, newTrainList);
                setArrivalAtSegmentHelper(cTempEntered.after, newTrainList);
                setArrivalAtSegmentHelper(cTempMap.before, collisionSet.getValue());
                setArrivalAtSegmentHelper(cTempMap.after, collisionSet.getValue());
            }
            else
                throw new TracksFullException("Facility " + name + " is full! Cannot schedule train!");
        }
    }

    private TimeInSegment isTimeInArrivals(TimeInSegment timeInFacility){
        if (trainInFacilityByTime.keySet() == null || timeInFacility == null)
            return null;
        for (TimeInSegment timMap : trainInFacilityByTime.keySet()) {
            TimeInSegment t = timMap.hasIntersection(timeInFacility);
            if (t != null)
                return (t);
        }
        //System.out.println("returning null!");
        return (null);
    }

    public TimeInSegment findKey(Train valor) {
        for (org.dei._Time.TimeInSegment key : trainInFacilityByTime.keySet()) {
            for (org.dei._Train.Train item : trainInFacilityByTime.get(key)) {
                if (item.equals(valor)) {
                    return key;
                }
            }
        }
        return null;
    }

    private void copyList(List<Train> from, List<Train> to){
        for (Train fromTrain : from) {
            to.add(fromTrain);
        }
    }

    private Map.Entry<TimeInSegment, List<Train>> getEntrySet(TimeInSegment collisionTime){
        Map.Entry<TimeInSegment, List<Train>> entryToSplit = null;
//        System.out.println("col = " + collisionTime.toString());
        for (Map.Entry<TimeInSegment, List<Train>> tmpEntry : trainInFacilityByTime.entrySet()) {
//            System.out.println("key = " + tmpEntry.getKey());
            if (tmpEntry.getKey().compareTo(collisionTime) == 0) {
                entryToSplit = tmpEntry;
                break;
            }
        }
        return (entryToSplit);
    }

    /**
     * Add a given railLine to the connections of this one
     * function will check if the given railLine has *this.id
     * in one of its ends, if not it will not be added
     * @param railLine RailLine to be added to HashMap
     */
    public void addConnectedFacility(RailLine railLine) {
        if (railLine.getEndFacility().getId() != this.getId()){
            if (railLine.getStartFacility().getId() == this.getId())
                connections.put(railLine.getEndFacility(), railLine);
        }
        else
            connections.put(railLine.getStartFacility(), railLine);
    }

    public Path createPath(List<Facility> railFacilities, Facility startFacility, Facility endFacility) {
        if (!railFacilities.contains(startFacility) || !railFacilities.contains(endFacility)) {
            throw new IllegalArgumentException("Start or end facility not found in the provided list");
        }

        for (int i = 0; i < railFacilities.size() - 1; i++) {
            Facility current = railFacilities.get(i);
            Facility next = railFacilities.get(i + 1);

            if (!current.getConnections().containsKey(next)) {
                throw new IllegalArgumentException("No direct connection between " + current.getName() + " and " + next.getName());
            }
        }

        Path path = new Path(railFacilities, startFacility, endFacility, "1"); // ! put a valid id on it!

        return path;
    }

    public List<Train> getTrainListInGivenTime(TimeInSegment time) {
        return (trainInFacilityByTime.get(time));
    }

    public TreeMap<TimeInSegment, List<Train>> getTimeInFacility(){
        return trainInFacilityByTime;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public GeographicalLocation getLocation() { return location; }
    public void setGeographicalLocation(GeographicalLocation l) { location = l; }
    public TimeZoneGroup getTimeZoneGroup() {
        return timeZoneGroup;
    }
    public TimeZone getTimeZone() {
        return timeZone;
    }
    public Country getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Facility facility = (Facility) obj;
        return (id == facility.getId());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

