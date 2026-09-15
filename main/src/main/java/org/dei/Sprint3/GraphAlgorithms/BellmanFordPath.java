package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BellmanFordPath {

    private final StationVertex orig;
    private final StationVertex dest;

    private final boolean hasNegativeCycle;

    private final List<StationVertex> shortestPath;
    private final Map<StationVertex, Double> distances;
    private final double totalCost;

    private final Set<StationVertex> negativeCycleVertices;
    private final List<Edge<StationVertex, TrackWeight>> negativeCycleEdges;

    public BellmanFordPath(StationVertex orig, StationVertex dest, boolean hasNegativeCycle, List<StationVertex> shortestPath, Map<StationVertex, Double> distances, double totalCost, Set<StationVertex> negativeCycleVertices, List<Edge<StationVertex, TrackWeight>> negativeCycleEdges) {
        this.orig = orig;
        this.dest = dest;
        this.hasNegativeCycle = hasNegativeCycle;
        this.shortestPath = shortestPath;
        this.distances = distances;
        this.totalCost = totalCost;
        this.negativeCycleVertices = negativeCycleVertices;
        this.negativeCycleEdges = negativeCycleEdges;
    }

    public StationVertex getOrig() { return orig; }

    public StationVertex getDest() { return dest; }

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    public List<StationVertex> getShortestPath() {
        return shortestPath;
    }

    public Map<StationVertex, Double> getDistances() {
        return distances;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Set<StationVertex> getNegativeCycleVertices() {
        return negativeCycleVertices;
    }

    public List<Edge<StationVertex, TrackWeight>> getNegativeCycleEdges() {
        return negativeCycleEdges;
    }
}
