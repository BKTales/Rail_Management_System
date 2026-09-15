package org.dei.Sprint2.Trees;

import org.dei._Location.GeographicalLocation;
import org.dei._Facilities.Station.Station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NodeData implements Comparable<NodeData> {
    protected GeographicalLocation coordinate;
    protected List<Station> stations;
    protected int height;

    public NodeData(GeographicalLocation coordinate, List<Station> stations) {
        this.coordinate = coordinate;
        this.stations = stations;
        this.height = 0;
    }

    public NodeData(GeographicalLocation coordinate, Station station) {
        this.coordinate = coordinate;
        this.stations = new ArrayList<>();
        this.stations.add(station);
        this.height = 0;
    }

    public NodeData(GeographicalLocation coordinate) {
        this.coordinate = coordinate;
        this.stations = new ArrayList<>();
        this.height = 0;
    }

    // copy node data
    public NodeData(NodeData nodeData){
        this.coordinate = nodeData.getCoordinate();
        this.stations = new ArrayList<>(nodeData.getStations()); // <- deep copy of the list
    }

    public GeographicalLocation getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(GeographicalLocation coordinate) {
        this.coordinate = coordinate;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public void incHeight(int height) { this.height++; }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public void addStation(Station station) {
        String newName = station.getName();
        int left = 0, right = stations.size() - 1;
        int insertIndex = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = stations.get(mid).getName().compareTo(newName);
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        insertIndex = left;
        stations.add(insertIndex, station);
    }


    static public Comparator<NodeData> getComparator(int num) {
        if (num % 2 == 0) {
            Comparator<NodeData> comparatorX = new Comparator<NodeData>() {
                @Override
                public int compare(NodeData o1, NodeData o2) {
                    return o1.compareLongitude(o2);
                }
            };
            return comparatorX;
        }
        Comparator<NodeData> comparatorY = new Comparator<NodeData>() {
            @Override
            public int compare(NodeData o1, NodeData o2) {
                return o1.compareLatitude(o2);
            }
        };
        return comparatorY;
    }

    private int compareLongitude(NodeData o){
        if(getCoordinate().getLongitude() < o.getCoordinate().getLongitude()){
            return -1;
        } else if(getCoordinate().getLongitude() > o.getCoordinate().getLongitude()){
            return 1;
        } else{
            return 0;
        }
    }

    private int compareLatitude(NodeData o){
        if (getCoordinate().getLatitude() < o.getCoordinate().getLatitude()){
            return -1;
        } else if(getCoordinate().getLatitude() > o.getCoordinate().getLatitude()){
            return 1;
        } else{
            return 0;
        }
    }

    @Override
    public int compareTo(NodeData other) {
        if (other == null) return 1;

        int cmpLat = Double.compare(this.coordinate.getLatitude(), other.coordinate.getLatitude());
        if (cmpLat != 0) return cmpLat;
        int cmpLon = Double.compare(this.coordinate.getLongitude(), other.coordinate.getLongitude());
        if (cmpLon != 0) return cmpLon;

        if (!this.stations.isEmpty() && !other.stations.isEmpty()) {
            return this.stations.get(0).getName().compareTo(other.stations.get(0).getName());
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("coordinates: ").append(coordinate);
        sb.append("\nStations: ");
        for (Station s : stations) {
            sb.append(s.getName()).append(", ");
        }
        return sb.toString();
    }
}
