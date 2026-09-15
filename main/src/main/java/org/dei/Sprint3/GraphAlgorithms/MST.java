package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Algorithms;
import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.dei.Sprint3.Graph.matrix.MatrixGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Minimum Spanning Tree Service
 * Provides utilities for MST computation on railway networks by length
 * Using kruskal because our railway networks is a spacious graph (not dense)
 */
public class MST {

    /**
     * Converts a directed graph to an undirected graph
     *
     * @param directedGraph Input directed graph
     * @return New undirected graph
     */
    public static <V> Graph<V, TrackWeight> convertToUndirected(Graph<V, TrackWeight> directedGraph){
        Graph<V, TrackWeight> undirectedGraph = createEmptyGraph(directedGraph, false);

        for(V vertex : directedGraph.vertices()){
            undirectedGraph.addVertex(vertex);
        }

        Set<String> addedEdges = new HashSet<>();

        for(Edge<V, TrackWeight> edge : directedGraph.edges()){
            V u = edge.getVOrig();
            V v = edge.getVDest();

            int uHash = u.hashCode();
            int vHash = v.hashCode();
            String edgeKey = Math.min(uHash, vHash) + "-" + Math.max(uHash, vHash);

            if(!addedEdges.contains(edgeKey)){
                undirectedGraph.addEdge(u, v, edge.getWeight());
                addedEdges.add(edgeKey);
            }
        }

        return undirectedGraph;
    }

    /**
     * Computes Minimum Spanning Tree using Kruskal's algorithm based on minimum total track length
     *
     * @param g Input undirected graph with TrackWeight
     * @return MST as a new graph
     */
    public static <V> Graph<V, TrackWeight> kruskal(Graph<V, TrackWeight> g){
        Graph<V, TrackWeight> mst = createEmptyGraph(g, false);

        for(V vertex : g.vertices()){
            mst.addVertex(vertex);
        }

        List<Edge<V, TrackWeight>> lstEdges = new ArrayList<>(g.edges());

        // Sort edges by distance (ascending)
        lstEdges.sort(Comparator.comparingDouble(e -> e.getWeight().getDistance()));

        for(Edge<V, TrackWeight> e : lstEdges){
            V vOrig = e.getVOrig();
            V vDest = e.getVDest();

            LinkedList<V> connectedVerts = Algorithms.DepthFirstSearch(mst, vOrig);

            if(connectedVerts == null || !connectedVerts.contains(vDest)){
                mst.addEdge(vOrig, vDest, e.getWeight());
            }
        }

        return mst;
    }

    private static <V> Graph<V, TrackWeight> createEmptyGraph(Graph<V, TrackWeight> g, boolean directed){
        if(g instanceof MapGraph){
            return new MapGraph<>(directed);
        } else if(g instanceof MatrixGraph){
            return new MatrixGraph<>(directed);
        }
        throw new IllegalArgumentException("Unsupported graph type: " + g.getClass().getName());
    }

    //=== export to .dot and .svg

    /**
     * Generates the DOT file and the corresponding SVG for the provided MST graph.
     *
     * @param mstGraph The MST graph (result of Kruskal's algorithm)
     * @param filename The base name for the files (e.g., "backbone" creates backbone.dot and backbone.svg)
     */
    public static void exportToGraphviz(Graph<StationVertex, TrackWeight> mstGraph, String filename) {
        String dotFile = filename + ".dot";
        String svgFile = filename + ".svg";

        try {
            String dotContent = generateDotContent(mstGraph);

            try (FileWriter writer = new FileWriter(dotFile)) {
                writer.write(dotContent);
            }

            generateSVG(dotFile, svgFile);

        } catch (IOException e) {
            System.err.println("Error writing DOT file: " + e.getMessage());
        }
    }

    private static String generateDotContent(Graph<StationVertex, TrackWeight> graph) {
        StringBuilder sb = new StringBuilder();

        sb.append("graph minimal_backbone {\n");

        sb.append("  bgcolor=\"white\";\n");
        sb.append("  node [shape=point, width=0.05, color=\"#0055A4\"];\n");
        sb.append("  edge [color=\"#444444\", penwidth=0.5];\n");
        sb.append("  overlap=false;\n");
        sb.append("  splines=false;\n\n");
        for (StationVertex v : graph.vertices()) {
            double x = v.getCoordinates().getX();
            double y = v.getCoordinates().getY();
            String name = v.getStation().getName().replace("\"", "\\\"");

            sb.append(String.format("  \"%d\" [pos=\"%.2f,%.2f!\", label=\"\", tooltip=\"%s\"];\n",
                    v.getStation().getId(), x, y, name));
        }

        sb.append("\n");

        Set<String> addedEdges = new HashSet<>();

        for (Edge<StationVertex, TrackWeight> edge : graph.edges()) {
            int id1 = edge.getVOrig().getStation().getId();
            int id2 = edge.getVDest().getStation().getId();

            String key = Math.min(id1, id2) + "-" + Math.max(id1, id2);

            if (!addedEdges.contains(key)) {
                sb.append(String.format("  \"%d\" -- \"%d\";\n", id1, id2));
                addedEdges.add(key);
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static void generateSVG(String dotPath, String svgPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder("neato", "-Tsvg", dotPath, "-o", svgPath);
            Process process = pb.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("⚠ Warning: 'neato' failed with code " + exitCode +
                        ". Check if Graphviz is installed.");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("⚠ Error executing Graphviz: " + e.getMessage());
            System.err.println("Install Graphviz to generate SVG (DOT file was created).");
        }
    }

}
