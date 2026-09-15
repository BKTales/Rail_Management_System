package org.dei.Repository;

import org.dei.Sprint3.Graph.RailNetworkGraphs.FacilityVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeightWithLine;
import org.dei.Sprint3.Graph.map.MapGraph;

public class GraphRepository {
    private static GraphRepository instance = null;
    private MapGraph<FacilityVertex, TrackWeightWithLine> graph;

    private GraphRepository(){
        graph = null;
    }

    public static GraphRepository getInstance() {
        if (instance == null) {
            instance = new GraphRepository();
        }
        return instance;
    }

    public MapGraph<FacilityVertex, TrackWeightWithLine> getGraph() { return graph; }

    public void setGraph(MapGraph<FacilityVertex, TrackWeightWithLine> graph) {
        this.graph = graph;
    }
}
