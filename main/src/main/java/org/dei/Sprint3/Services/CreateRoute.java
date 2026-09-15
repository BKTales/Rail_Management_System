package org.dei.Sprint3.Services;

import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei.Sprint3.Graph.Algorithms;
import org.dei.Sprint3.Graph.RailNetworkGraphs.FacilityVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeightWithLine;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.util.LinkedList;
import java.util.List;

public class CreateRoute {

    private static int countPath = 0;

    public static List<Facility> shortestPath(MapGraph<FacilityVertex, TrackWeightWithLine> graph, FacilityVertex vFrom, FacilityVertex vTo) {
        LinkedList<FacilityVertex> facilityVertices = new LinkedList<>();
        List<Facility> facility = new LinkedList<>();

        Algorithms.<FacilityVertex, TrackWeightWithLine> shortestPath(graph, vFrom, vTo, TrackWeightWithLine::compareTo,
                TrackWeightWithLine::apply, new TrackWeightWithLine(0, 0, 0), facilityVertices);

        for (FacilityVertex v : facilityVertices)
            facility.add(v.getStation());
        return (facility);
    }

    public static Path generateAutomaticPath(MapGraph<FacilityVertex, TrackWeightWithLine> graph,
                                              FacilityVertex vFrom, FacilityVertex vTo)
    {
        String autoCounter = "AUTO_PATH_";
        countPath++;

        return new Path(shortestPath(graph, vFrom, vTo), vFrom.getStation(), vTo.getStation(), autoCounter + countPath);
    }
}
