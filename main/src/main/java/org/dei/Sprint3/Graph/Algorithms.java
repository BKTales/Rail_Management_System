package org.dei.Sprint3.Graph;

import org.dei.Sprint3.Graph.matrix.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {

    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        LinkedList<V> resultado = new LinkedList<>();
        LinkedList<V> queue = new LinkedList<>();
        HashSet<V> visited = new HashSet<>();

        if(!g.validVertex(vert)){
            return null;
        }

        queue.add(vert);
        visited.add(vert);

        while (!queue.isEmpty()) {
            V aux = queue.removeFirst();
            resultado.add(aux);

            for(V adj: g.adjVertices(aux)){
                if(!visited.contains(adj)){
                    queue.add(adj);
                    visited.add(adj);
                }
            }
        }
        return resultado;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, Set<V> visited, LinkedList<V> qdfs) {
        visited.add(vOrig);
        qdfs.add(vOrig);

        for (V neighbor : g.adjVertices(vOrig)) {
            if (!visited.contains(neighbor)) {
                DepthFirstSearch(g, neighbor, visited, qdfs);
            }
        }
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search
     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> result = new LinkedList<>();
        Set<V> visited = new HashSet<>();
        DepthFirstSearch(g, vert, visited, result);

        return result;
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, Set<V> visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        visited.add(vOrig);
        path.add(vOrig);

        if (vOrig.equals(vDest)) {
            // Found a path - add a copy to the results
            paths.add(new LinkedList<>(path));
        } else {
            for (V neighbor : g.adjVertices(vOrig)) {
                if (!visited.contains(neighbor)) {
                    allPaths(g, neighbor, vDest, visited, path, paths);
                }
            }
        }

        // Backtrack
        path.removeLast();
        visited.remove(vOrig);
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return new ArrayList<>();
        }

        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        LinkedList<V> path = new LinkedList<>();

        allPaths(g, vOrig, vDest, visited, path, paths);
        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param ce       comparator between elements of type E
     * @param sum      sum two elements of type E
     * @param zero     neutral element of the sum in elements of type E
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V[] pathKeys, E[] dist) {
        int numVertices = g.numVertices();
        int origIndex = g.key(vOrig);

        // Initialize arrays
        for (int i = 0; i < numVertices; i++) {
            dist[i] = null; // represents infinity
            pathKeys[i] = null;
            visited[i] = false;
        }

        dist[origIndex] = zero;

        for (int count = 0; count < numVertices - 1; count++) {
            int u = -1;
            E minDist = null;

            // Find vertex with minimum distance
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && dist[i] != null && (minDist == null || ce.compare(dist[i], minDist) < 0)) {
                    minDist = dist[i];
                    u = i;
                }
            }

            if (u == -1) break;

            visited[u] = true;
            V uVertex = g.vertex(u);

            // Update distances for adjacent vertices
            for (V v : g.adjVertices(uVertex)) {
                int vIndex = g.key(v);
                if (!visited[vIndex]) {
                    E edgeWeight = g.edge(uVertex, v).getWeight();
                    E newDist = sum.apply(dist[u], edgeWeight);

                    if (dist[vIndex] == null || ce.compare(newDist, dist[vIndex]) < 0) {
                        dist[vIndex] = newDist;
                        pathKeys[vIndex] = uVertex;
                    }
                }
            }
        }
    }

    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        int numVertices = g.numVertices();
        boolean[] visited = new boolean[numVertices];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[numVertices];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVertices];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        int destIndex = g.key(vDest);
        if (dist[destIndex] == null) {
            return null; // No path exists
        }

        // Reconstruct the path
        getPath(g, vOrig, vDest, pathKeys, shortPath);
        return dist[destIndex];
    }

    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {
        if (!g.validVertex(vOrig)) {
            return false;
        }

        int numVertices = g.numVertices();
        boolean[] visited = new boolean[numVertices];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[numVertices];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVertices];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        // Populate results
        paths.clear();
        dists.clear();

        for (int i = 0; i < numVertices; i++) {
            V vDest = g.vertex(i);
            LinkedList<V> path = new LinkedList<>();

            if (dist[i] != null) {
                getPath(g, vOrig, vDest, pathKeys, path);
            }

            paths.add(path);
            dists.add(dist[i]);
        }

        return true;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V[] pathKeys, LinkedList<V> path) {
        path.clear();

        if (vOrig.equals(vDest)) {
            path.add(vOrig);
            return;
        }

        // Reconstruct path in reverse order
        LinkedList<V> reversePath = new LinkedList<>();
        V current = vDest;

        while (current != null && !current.equals(vOrig)) {
            reversePath.addFirst(current);
            int currentIndex = g.key(current);
            current = pathKeys[currentIndex];
        }

        if (current != null && current.equals(vOrig)) {
            path.add(vOrig);
            path.addAll(reversePath);
        }
    }

    /** Calculates the minimum distance graph using Floyd-Warshall
     *
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @return the minimum distance graph
     */
    public static <V,E> MatrixGraph<V,E> minDistGraph(Graph<V,E> g, Comparator<E> ce, BinaryOperator<E> sum, E zero) {
        int numVertices = g.numVertices();
        @SuppressWarnings("unchecked")
        E[][] dist = (E[][]) new Object[numVertices][numVertices];

        // Initialize distance matrix
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    dist[i][j] = zero;
                } else {
                    V vi = g.vertex(i);
                    V vj = g.vertex(j);
                    Edge<V, E> edge = g.edge(vi, vj);
                    dist[i][j] = (edge != null) ? edge.getWeight() : null;
                }
            }
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] != null && dist[k][j] != null) {
                        E throughK = sum.apply(dist[i][k], dist[k][j]);
                        if (dist[i][j] == null || ce.compare(throughK, dist[i][j]) < 0) {
                            dist[i][j] = throughK;
                        }
                    }
                }
            }
        }

        // Create result graph
        MatrixGraph<V, E> result = new MatrixGraph<>(g.isDirected());
        for (V vertex : g.vertices()) {
            result.addVertex(vertex);
        }

        // Add edges to result graph
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (dist[i][j] != null && !dist[i][j].equals(zero)) {
                    V vi = g.vertex(i);
                    V vj = g.vertex(j);
                    result.addEdge(vi, vj, dist[i][j]);
                }
            }
        }

        return result;
    }


}