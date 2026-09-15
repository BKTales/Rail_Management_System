package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.util.*;

public class DirectedLineUpgradePlan {

    public static UpgradePlanResult computeUpgradePlan(Graph<StationVertex, TrackWeight> graph) {
        if (graph == null || !graph.isDirected()) {
            throw new IllegalArgumentException("Graph must be non-null and directed");
        }


        List<Graph<StationVertex, TrackWeight>> WCCs = getWeaklyConnectedComponents(graph);

        List<LinkedList<StationVertex>> finalTopologicalOrders = new LinkedList<>();


        for (Graph<StationVertex, TrackWeight> wcc : WCCs) {
            LinkedList<StationVertex> finishStack = new LinkedList<>();
            LinkedList<StationVertex> cycleResult = new LinkedList<>();


            kosarajuAlgorithm(wcc, cycleResult, finishStack);

            if (!cycleResult.isEmpty()) {
                return new UpgradePlanResult(true, null, cycleResult);
            }

            Collections.reverse(finishStack);
            finalTopologicalOrders.add(finishStack);
        }


        return new UpgradePlanResult(false, finalTopologicalOrders , null);
    }


    public static void kosarajuAlgorithm(Graph<StationVertex, TrackWeight> graph,
                                            LinkedList<StationVertex> cycle,
                                            LinkedList<StationVertex> finishStack) {

        boolean[] visited = new boolean[graph.numVertices()];

        computeFinishTimes(graph, visited, finishStack);

        Graph<StationVertex, TrackWeight> tGraph = reverseGraph(graph);
        findSCCsAndCycle(tGraph, finishStack, cycle, visited);
    }

    private static void computeFinishTimes(
            Graph<StationVertex, TrackWeight> graph, boolean[] visited, LinkedList<StationVertex> finishStack) {

        for (int i = 0; i < graph.numVertices(); i++) {
            StationVertex v = graph.vertex(i);
            if (!visited[graph.key(v)]) {
                DFSFinishTime(graph, v, visited, finishStack);
            }
        }
    }

    private static void findSCCsAndCycle(
            Graph<StationVertex, TrackWeight> tGraph, LinkedList<StationVertex> finishStack, LinkedList<StationVertex> cycle, boolean[] visited) {

        Arrays.fill(visited, false);

        LinkedList<StationVertex> tempStack = new LinkedList<>(finishStack);

        while (!tempStack.isEmpty()) {
            StationVertex v = tempStack.removeLast();
            if (!visited[tGraph.key(v)]) {
                LinkedList<StationVertex> SCC = new LinkedList<>();

                DFSFinishTime(tGraph, v, visited, SCC);

                if (SCC.size() > 1) {
                    cycle.addAll(SCC);
                    return;
                }
            }
        }
    }



    private static <V, E> void DFSFinishTime(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        int vKey = g.key(vOrig);


        if(vKey < 0 || vKey >= visited.length) return;

        visited[vKey] = true;
        for (V neighbor : g.adjVertices(vOrig)) {
            int neighborKey = g.key(neighbor);
            if (neighborKey >= 0 && neighborKey < visited.length && !visited[neighborKey]) {
                DFSFinishTime(g, neighbor, visited, qdfs);
            }
        }
        qdfs.add(vOrig);
    }


    public static Graph<StationVertex, TrackWeight> reverseGraph(Graph<StationVertex, TrackWeight> graph) {
        Graph<StationVertex, TrackWeight> tGraph = new MapGraph<>(graph.isDirected());

        for (StationVertex v : graph.vertices()) {
            tGraph.addVertex(v);
        }

        for (Edge<StationVertex,TrackWeight> edge : graph.edges()) {
            tGraph.addEdge(edge.getVDest(), edge.getVOrig(), edge.getWeight());
        }

        return tGraph;
    }


    public static List<Graph<StationVertex, TrackWeight>> getWeaklyConnectedComponents(
            Graph<StationVertex, TrackWeight> graph) {

        Graph<StationVertex, TrackWeight> undirectedGraph = buildUndirectedGraph(graph);

        boolean[] visited = new boolean[graph.numVertices()];
        List<Graph<StationVertex, TrackWeight>> wccs = new ArrayList<>();

        for (int i = 0; i < undirectedGraph.numVertices(); i++) {
            StationVertex v = undirectedGraph.vertex(i);
            if (!visited[undirectedGraph.key(v)]) {
                Set<StationVertex> componentVertices = new HashSet<>();
                dfs(undirectedGraph, v, visited, componentVertices);

                Graph<StationVertex, TrackWeight> wccGraph = reconstructWCCGraph(graph, componentVertices);

                wccs.add(wccGraph);
            }
        }
        return wccs;
    }

    private static Graph<StationVertex, TrackWeight> buildUndirectedGraph(Graph<StationVertex, TrackWeight> graph) {
        Graph<StationVertex, TrackWeight> undirectedGraph = new MapGraph<>(false);
        for (StationVertex v : graph.vertices()) {
            undirectedGraph.addVertex(v);
        }
        for (Edge<StationVertex, TrackWeight> edge : graph.edges()) {
            undirectedGraph.addEdge(edge.getVOrig(), edge.getVDest(), edge.getWeight());
        }
        return undirectedGraph;
    }

    private static Graph<StationVertex, TrackWeight> reconstructWCCGraph(
            Graph<StationVertex, TrackWeight> originalGraph, Set<StationVertex> componentVertices) {

        Graph<StationVertex, TrackWeight> wccGraph = new MapGraph<>(originalGraph.isDirected());

        for (StationVertex cv : componentVertices) {
            wccGraph.addVertex(cv);
        }

        for (Edge<StationVertex, TrackWeight> edge : originalGraph.edges()) {
            if (componentVertices.contains(edge.getVOrig()) && componentVertices.contains(edge.getVDest())) {
                wccGraph.addEdge(edge.getVOrig(), edge.getVDest(), edge.getWeight());
            }
        }
        return wccGraph;
    }

    private static void dfs(Graph<StationVertex, TrackWeight> graph,
                                  StationVertex v, boolean[] visited,
                                  Set<StationVertex> component) {
        int vKey = graph.key(v);
        if (vKey < 0 || vKey >= visited.length || visited[vKey]) return;

        visited[vKey] = true;
        component.add(v);

        for (StationVertex neighbor : graph.adjVertices(v)) {
            if (!visited[graph.key(neighbor)]) {
                dfs(graph, neighbor, visited, component);
            }
        }
    }
}