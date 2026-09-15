package org.dei._Path;

import org.dei._Facilities.Facility;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Facility>      railFacilities;
    private final Facility            startFacility;
    private final Facility            endFacility;
    private final String              pathId;

    public Path(List<Facility> railFacilities, Facility startFacility, Facility endFacility, String pathId) {
        this.railFacilities = railFacilities;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.pathId = pathId;
    }

    public Path(Facility startFacility, Facility endFacility, String pathId) {
        this.railFacilities = new ArrayList<>();
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.pathId = pathId;
    }


    public String            getPathId() { return pathId; }
    public Facility          getEndFacility() { return endFacility; }
    public Facility          getStartFacility() { return startFacility; }
    public List<Facility>    getRailFacilities() { return railFacilities; }
    public void setRailFacilities(List<Facility> path) {
        this.railFacilities.clear();
        this.railFacilities.addAll(path);
    }

    @Override
    public String toString() {
        return "Path{" +
                "railFacilities=" + railFacilities +
                ", startFacility=" + startFacility +
                ", endFacility=" + endFacility +
                ", pathId='" + pathId + '\'' +
                '}';
    }
}
