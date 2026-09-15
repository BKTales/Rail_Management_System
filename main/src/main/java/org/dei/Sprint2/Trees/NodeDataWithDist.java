package org.dei.Sprint2.Trees;

import org.dei._Facilities.Station.Station;

import java.util.Comparator;

public class NodeDataWithDist extends NodeData{
    private double distance;

    public NodeDataWithDist(NodeData nodeNata, double distance){
        super(nodeNata);
        this.distance = distance;
        stations.sort(new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                return  o1.getName().compareTo(o2.getName()) * -1;
            }
        });

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(NodeData other) {
        if (other instanceof NodeDataWithDist){
            NodeDataWithDist otherWithDist = (NodeDataWithDist) other;
            if(this.distance == otherWithDist.getDistance()) return 0;
            else if (this.distance < otherWithDist.distance) return -1;
            else return 1;
        }
        return 0;
    }
}
