package org.dei._RailLineNetwork;

import org.dei._Facilities.Facility;
import java.util.ArrayList;
import java.util.List;

public class RailLine {
    private final String            railLineId;
    private final Facility          startFacility;
    private final Facility          endFacility;
    private List<RailSegment>       railSegments;
    private double                  totalDistance;
    private final String            name;
    private final String            owner;

    public RailLine(String routeId, Facility startFacility, Facility endFacility, List<RailSegment> railSegments, String name, String owner) {
        this.railLineId = routeId;
        this.railSegments = railSegments;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.name = name;
        this.owner = owner;
        totalDistance = calculateTotalDistance();
    }

    public RailLine(String routeId, Facility startFacility, Facility endFacility, String name, String owner) {
        this.railLineId = routeId;
        this.railSegments = new ArrayList<>();
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.name = name;
        this.owner = owner;
        totalDistance = calculateTotalDistance();
    }

    public RailLine(String railLineId, Facility startFacility, Facility endFacility, List<RailSegment> railSegments) {
        this.railLineId = railLineId;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.railSegments = new ArrayList<>();

        if (railSegments != null) {
            for(RailSegment rs : railSegments) {
                this.railSegments.add(rs);
                //addSegment(rs);
            }
        }
        name = "";
        owner = "";
    }

    public RailLine(String railLineId, Facility startFacility, Facility endFacility) {
        this.railLineId = railLineId;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.railSegments = new ArrayList<>();
        this.totalDistance = 0;
        name = "";
        owner = "";
    }

    private double calculateTotalDistance(){
        double tempDistance = 0;
        for (RailSegment railSegment : railSegments) {
            tempDistance += railSegment.getDistanceKm();
        }

        return tempDistance;
    }
    
    public void addSegment(RailSegment segment){
        railSegments.add(segment);
        totalDistance += segment.getDistanceKm();
    }

    public String getName() { return name; }
    public String getOwner() { return owner; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public void setRailSegments(List<RailSegment> railSegments) { this.railSegments = railSegments; }
    public String getGauge() {
        //if (railSegments.isEmpty()) return null;
        //return railSegments.get(0).getGauge();
        return null;
    }

    public int getMediumCapacity() { return railSegments.size(); } // change it later

    public String getRailLineId() { return railLineId; }
    public Facility getStartFacility() { return startFacility; }
    public Facility getEndFacility() { return endFacility; }
    public List<RailSegment> getRailSegments() { return railSegments; }
    public double getTotalDistance() { return totalDistance; }

    @Override
    public String toString() {
        return "RailLine{" + railLineId + ", gauge=" + getGauge() + "}";
    }
}