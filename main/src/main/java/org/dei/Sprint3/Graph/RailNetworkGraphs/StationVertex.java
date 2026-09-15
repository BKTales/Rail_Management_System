package org.dei.Sprint3.Graph.RailNetworkGraphs;

import org.dei._Facilities.Station.Station;

public class StationVertex implements Comparable<StationVertex> {
    private CartesianCoordinates coordinates;
    private Station station;

    public StationVertex(CartesianCoordinates coordinates, Station station) {
        this.coordinates = coordinates;
        this.station = station;
    }



    public CartesianCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CartesianCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
    @Override
    public String toString() {
        return station.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StationVertex)) return false;
        StationVertex other = (StationVertex) obj;
        return this.compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(station.getId());
    }


    @Override
    public int compareTo(StationVertex o) {
        if (this.coordinates.equals(o.getCoordinates()) && this.station.equals(o.getStation()))
            return 0;
        return 1;
    }
}
