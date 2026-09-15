package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;

import java.util.LinkedList;
import java.util.List;

public class UpgradePlanResult {
    private final boolean hasCycle;
    private final List<LinkedList<StationVertex>> topologicalOrder; // null if cycles exist
    private final LinkedList<StationVertex> cycle; // null if acyclic

    public UpgradePlanResult(boolean hasCycle, List<LinkedList<StationVertex>> topologicalOrder,
                              LinkedList<StationVertex> cycle) {
        this.hasCycle = hasCycle;
        this.topologicalOrder = topologicalOrder;
        this.cycle = cycle;
    }

    public boolean hasCycle() { return hasCycle; }
    public List<LinkedList<StationVertex>> getTopologicalOrder() { return topologicalOrder; }
    public LinkedList<StationVertex> getCycle() { return cycle; }
}
