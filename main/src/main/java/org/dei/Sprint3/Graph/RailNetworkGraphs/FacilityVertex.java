package org.dei.Sprint3.Graph.RailNetworkGraphs;

import org.dei._Facilities.Facility;
import org.dei._Location.GeographicalLocation;

public class FacilityVertex implements Comparable<FacilityVertex> {
    private GeographicalLocation location;
    private Facility station;

    public FacilityVertex(Facility station) {
        this.location = station.getLocation();
        this.station = station;
    }

    public GeographicalLocation getLocation() {
        return location;
    }

    public void setCoordinates(GeographicalLocation location) {
        this.location = location;
    }

    public Facility getStation() {
        return station;
    }

    public void setStation(Facility station) {
        this.station = station;
    }
    @Override
    public String toString() {
        return station.toString();
    }

    public boolean equals(FacilityVertex o){
        return this.compareTo(o) == 0;
    }

    @Override
    public int compareTo(FacilityVertex o) {
        if (this.location.equals(o.getLocation()) && this.station.equals(o.getStation()))
            return 0;
        return 1;
    }
}
