package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;

import java.util.*;

/**
 * Implementation of the Ford-Fulkerson algorithm to calculate the Maximum Flow
 * between two Hubs (Stations) in a railway network.
 * * <p><strong>Complexity Analysis:</strong></p>
 * <ul>
 * <li><strong>Time Complexity:</strong> O(E * f*), where E is the number of edges and f* is the maximum flow value.
 * Since this implementation uses DFS, in the worst-case scenario (with integer capacities),
 * each iteration might only increase the flow by 1 unit.</li>
 * <li><strong>Space Complexity:</strong> O(V + E) to maintain the residual graph structure and visited sets.</li>
 * </ul>
 */
public class FordFulkerson {

    private final StationVertex source;
    private final StationVertex sink;
    private double maxFlow;

    // Residual Graph: Maps Source Vertex -> (Target Vertex -> Residual Capacity)
    // We use this Map structure to strictly avoid modifying the original Rail Network Graph.
    private final Map<StationVertex, Map<StationVertex, Double>> residualGraph;

    /**
     * Constructs the algorithm instance and immediately computes the Maximum Flow.
     *
     * @param graph  The railway network graph loaded from CSVs
     * @param source The starting station (Source Hub)
     * @param sink   The destination station (Sink Hub)
     * @throws IllegalArgumentException if graph or stations are null
     */
    public FordFulkerson(Graph<StationVertex, TrackWeight> graph, StationVertex source, StationVertex sink) {
        if (graph == null || source == null || sink == null) {
            throw new IllegalArgumentException("Graph, Source, and Sink cannot be null.");
        }

        this.source = source;
        this.sink = sink;
        this.maxFlow = 0.0;
        this.residualGraph = new HashMap<>();

        initializeResidualGraph(graph);

        computeMaxFlow();
    }

    /**
     * Initializes the residual graph structure.
     * It maps the original capacities to forward edges and creates backward edges with 0 capacity.
     */
    private void initializeResidualGraph(Graph<StationVertex, TrackWeight> graph) {
        for (Edge<StationVertex, TrackWeight> edge : graph.edges()) {
            StationVertex u = edge.getVOrig();
            StationVertex v = edge.getVDest();

            double capacity = 0.0;
            if (edge.getWeight() != null) {
                capacity = edge.getWeight().getCapacity();
            }

            residualGraph.computeIfAbsent(u, k -> new HashMap<>()).put(v, capacity);

            residualGraph.computeIfAbsent(v, k -> new HashMap<>()).putIfAbsent(u, 0.0);
        }
    }

    /**
     * Core Ford-Fulkerson logic.
     * Repeatedly finds a path using DFS and augments flow along that path
     * until no valid path from Source to Sink exists.
     */
    private void computeMaxFlow() {
        Map<StationVertex, StationVertex> parent = new HashMap<>();

        // Loop: While there is a path from source to sink in the residual graph
        while (dfs(parent)) {

            double pathFlow = Double.MAX_VALUE;
            StationVertex v = sink;

            while (v != source) {
                StationVertex u = parent.get(v);
                double currentCap = residualGraph.get(u).get(v);
                pathFlow = Math.min(pathFlow, currentCap);
                v = u;
            }

            if (pathFlow == 0) break;

            this.maxFlow += pathFlow;

            v = sink;
            while (v != source) {
                StationVertex u = parent.get(v);

                double oldForward = residualGraph.get(u).get(v);
                residualGraph.get(u).put(v, oldForward - pathFlow);

                double oldBackward = residualGraph.get(v).get(u);
                residualGraph.get(v).put(u, oldBackward + pathFlow);

                v = u;
            }
        }
    }

    /**
     * Iterative Depth-First Search (DFS) to find a path from Source to Sink.
     * We use an iterative approach (Stack) to prevent StackOverflowError on large railway networks.
     *
     * @param parent A map that will be populated with the path (Child -> Parent)
     * @return true if a valid path to the Sink is found, false otherwise
     */
    private boolean dfs(Map<StationVertex, StationVertex> parent) {
        parent.clear();
        Set<StationVertex> visited = new HashSet<>();
        Stack<StationVertex> stack = new Stack<>();

        stack.push(source);
        visited.add(source);
        parent.put(source, null);

        while (!stack.isEmpty()) {
            StationVertex u = stack.pop();

            // If we reached the Sink, a path exists
            if (u.equals(sink)) {
                return true;
            }

            Map<StationVertex, Double> neighbors = residualGraph.get(u);
            if (neighbors == null) continue;

            for (Map.Entry<StationVertex, Double> entry : neighbors.entrySet()) {
                StationVertex v = entry.getKey();
                double residualCap = entry.getValue();

                if (!visited.contains(v) && residualCap > 0) {
                    parent.put(v, u);
                    visited.add(v);
                    stack.push(v);
                }
            }
        }

        return false;
    }

    /**
     * Returns the calculated Maximum Flow.
     * @return The maximum theoretical throughput between source and sink.
     */
    public double getMaxFlow() {
        return maxFlow;
    }

    public StationVertex getSource() {
        return source;
    }

    public StationVertex getSink() {
        return sink;
    }
}