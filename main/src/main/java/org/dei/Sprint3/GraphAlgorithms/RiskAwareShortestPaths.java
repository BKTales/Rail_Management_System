package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.util.*;

public class RiskAwareShortestPaths {

    public static BellmanFordPath getBellmanFordPath(MapGraph<StationVertex, TrackWeight> graph, StationVertex orig, StationVertex dest) {

        Map<StationVertex, Double> distance = new HashMap<>();
        Map<StationVertex, StationVertex> predecessor = new HashMap<>();

        for (StationVertex v : graph.vertices()) {
            distance.put(v, Double.POSITIVE_INFINITY);
        }
        distance.put(orig, 0.0);

        int V = graph.numVertices();

        for (int i = 1; i <= V - 1; i++) {
            for (Edge<StationVertex, TrackWeight> edge : graph.edges()) {
                StationVertex u = edge.getVOrig();
                StationVertex v = edge.getVDest();
                double w = edge.getWeight().getCost();
                if (distance.get(u) != Double.POSITIVE_INFINITY && distance.get(u) + w < distance.get(v)) {
                    distance.put(v, distance.get(u) + w);
                    predecessor.put(v, u);
                }
            }
        }

        // verify if there is a negative cycle
        Edge<StationVertex, TrackWeight> cycleEdge = null;

        for (Edge<StationVertex, TrackWeight> edge : graph.edges()) {
            StationVertex u = edge.getVOrig();
            StationVertex v = edge.getVDest();
            double w = edge.getWeight().getCost();

            if (distance.get(u) != Double.POSITIVE_INFINITY && distance.get(u) + w < distance.get(v)) {
                cycleEdge = edge;
                break;
            }
        }

        if (cycleEdge != null) {

            StationVertex cycleVertex = cycleEdge.getVDest();

            for (int i = 0; i < graph.numVertices(); i++) {
                cycleVertex = predecessor.get(cycleVertex);
            }

            List<Edge<StationVertex, TrackWeight>> cycleEdges = new ArrayList<>();
            Set<StationVertex> cycleVertices = new LinkedHashSet<>();

            StationVertex current = cycleVertex;

            do {
                StationVertex prev = predecessor.get(current);

                if (prev == null) break;

                for (Edge<StationVertex, TrackWeight> e : graph.edges()) {
                    if (e.getVOrig().equals(prev) && e.getVDest().equals(current)) {
                        cycleEdges.add(e);
                        break;
                    }
                }

                cycleVertices.add(current);
                current = prev;

            } while (!current.equals(cycleVertex));

            return new BellmanFordPath(orig, dest, true, new ArrayList<>(), distance, Double.NEGATIVE_INFINITY, cycleVertices, cycleEdges);
        }

        if (distance.get(dest) == Double.POSITIVE_INFINITY) {
            return new BellmanFordPath(orig, dest,false, new ArrayList<>(), distance, Double.POSITIVE_INFINITY, new HashSet<>(), new ArrayList<>());
        }

        // rebuild path
        List<StationVertex> path = new LinkedList<>();
        StationVertex current = dest;
        while (current != null) {
            path.addFirst(current);
            current = predecessor.get(current);
        }

        // return the bellman shortest path between the two stations
        return new BellmanFordPath(orig, dest, false, path, distance, distance.get(dest), null, null);
    }

    public static void printBellmanFordPath(BellmanFordPath path) {

        StringBuilder pathOutput = new StringBuilder();

        pathOutput.append("-x-x-x-x-x-x- Shortest path between stations ")
                .append(path.getOrig().getStation().getName())
                .append(" and ")
                .append(path.getDest().getStation().getName())
                .append(" -x-x-x-x-x-x-\n\n");

        if (path.hasNegativeCycle()) {
            int cont = 0;

            pathOutput.append("===== WARNING: A negative cycle was detected! =====\n");
            pathOutput.append("Edges involved in the negative cycle:\n");

            for (Edge<StationVertex, TrackWeight> negativeEdge : path.getNegativeCycleEdges()) {
                cont++;
                pathOutput.append("   ")
                        .append(cont)
                        .append(". ")
                        .append(negativeEdge.getVOrig().getStation().getName())
                        .append(" --> ")
                        .append(negativeEdge.getVDest().getStation().getName())
                        .append("\n");
            }

        } else {
            if(path.getShortestPath().isEmpty()){
                pathOutput.append("No path found between the selected stations.");
            } else {
                pathOutput.append("Path (station - accumulated cost):\n");

                for (StationVertex station : path.getShortestPath()) {
                    pathOutput.append("   ")
                            .append(station.getStation().getName()).append(" - ")
                            .append(String.format("%.3f", path.getDistances().get(station)))
                            .append(" KM")
                            .append("\n");
                }

                pathOutput.append("\nTotal cost between the stations: ")
                        .append(String.format("%.3f", path.getTotalCost()))
                        .append(" KM")
                        .append("\n");
            }
        }

        System.out.println(pathOutput);
    }
}
