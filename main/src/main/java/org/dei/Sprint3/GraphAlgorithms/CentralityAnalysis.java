package org.dei.Sprint3.GraphAlgorithms;

import org.dei.Sprint3.Graph.Algorithms;
import org.dei.Sprint3.Graph.Edge;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CentralityAnalysis {
    public static class ResponseDto {
        public String   stationName;
        public int      stationId;
        public int      degree;
        public double   strength;
        public double   hubScore;
        public double   betweenness;
        public double   harmonicCloseness;
    }

    private static void buildShortPath(MapGraph<StationVertex, TrackWeight> g, LinkedList<StationVertex> shortPath, int vOrig, int vDest, int[] vertexes){
        int index = vDest;

        while (index != vOrig)
        {
            // System.out.println("c " + vOrig + " " + index);
            shortPath.addFirst(g.vertex(index));
            index = vertexes[index];
        }
        shortPath.addFirst(g.vertex(index));
    }

    public static boolean bellmanFord(MapGraph<StationVertex, TrackWeight> g, StationVertex vOrig,
                                      StationVertex vDest,  LinkedList<StationVertex> shortPath) {
        int numVertices = g.numVertices();
        double[] cost = new double[numVertices];
        int[] prev = new int[numVertices];

        // Initialize
        Arrays.fill(cost, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        int origKey = g.key(vOrig);
        int destKey = g.key(vDest);
        cost[origKey] = 0;

        // Relax edges up to V-1 times with early termination
        for (int i = 0; i < numVertices - 1; i++) {
            boolean updated = false;
            for (Edge<StationVertex, TrackWeight> edge : g.edges()) {
                int vFrom = g.key(edge.getVOrig());
                int vTo = g.key(edge.getVDest());
                double weight = edge.getWeight().getCost();

                if (cost[vFrom] != Double.POSITIVE_INFINITY &&
                        cost[vFrom] + weight < cost[vTo]) {
                    cost[vTo] = cost[vFrom] + weight;
                    prev[vTo] = vFrom;
                    updated = true;
                }
            }
            if (!updated) break;  // Early termination
        }

        // Check if destination is unreachable
        if (cost[destKey] == Double.POSITIVE_INFINITY)
            return (false);

        // Detect negative cycles
        for (Edge<StationVertex, TrackWeight> edge : g.edges()) {
            int vFrom = g.key(edge.getVOrig());
            int vTo = g.key(edge.getVDest());
            double weight = edge.getWeight().getCost();

            if (cost[vFrom] != Double.POSITIVE_INFINITY &&
                    cost[vFrom] + weight < cost[vTo]) {
                return (false);  // Negative cycle detected
            }
        }

        buildShortPath(g, shortPath, origKey, destKey, prev);
        return true;  // Valid path found
    }

    /**
     * Function will get all the shortest paths in graph and get which is the
     * index where the vOrig has any connection to other stations
     *
     * @param g
     * @return list with all shortest paths in graph
     */
    public static List<LinkedList<StationVertex>> getALLShortestPaths(MapGraph<StationVertex, TrackWeight> g) {
        StationVertex   vTo;
        StationVertex   vFrom;
        int             manyNodes = g.numVertices();
        LinkedList<StationVertex> path;
        List<StationVertex> listOfNodes = g.vertices();
        List<LinkedList<StationVertex>> listOfPaths = new ArrayList<>();

        for (int i = 0; i < manyNodes; i++)
        {
            vFrom = listOfNodes.get(i);
            for (int j = i + 1; j < manyNodes; j++)
            {
                vTo = listOfNodes.get(j);
                path = new LinkedList<StationVertex>();
                if (bellmanFord(g, vFrom, vTo, path))
                    listOfPaths.addLast(path);
            }
        }
        return (listOfPaths);
    }

    public static boolean hasThisStationInPath(LinkedList<StationVertex> path , StationVertex cmp) {
        for (StationVertex vertex : path)
            if (vertex.equals(cmp))
                return (true);
        return (false);
    }

    /**
     * Function will search in all paths for a path betweeen vOrig and vFrom,
     * if found it will calculate its distance
     * @param allPath
     * @param vOrig
     * @param vFrom
     * @return (if found - distance of shortestPath between vOrigin and vFrom)
     * @return (not found - 0 )
     */
    private static double containsPath(MapGraph<StationVertex, TrackWeight> g, List<LinkedList<StationVertex>> allPath,
                                       StationVertex vOrig, StationVertex vFrom)
    {
        double total = 0;

        for (LinkedList<StationVertex> currPath : allPath)
        {
            if ((currPath.getFirst().equals(vOrig) && currPath.getLast().equals(vFrom))
                || (currPath.getFirst().equals(vFrom) && currPath.getLast().equals(vOrig)) )
            {
               for (int j = 0; j < currPath.size() - 1; j++)
               {
                   StationVertex v1 = currPath.get(j);
                   StationVertex vTo = currPath.get(j + 1);
                   total += g.edge(v1, vTo).getWeight().getDistance();
               }
               return (total);
            }
        }
        return (0);
    }

    public static int countManyPathsWithoutOriginInStartOrEnd(List<LinkedList<StationVertex>> allPath, StationVertex vOrig)
    {
        int count = allPath.size();
        for (LinkedList<StationVertex> path : allPath)
        {
            if (path.getFirst().equals(vOrig) || path.getLast().equals(vOrig))
                count--;
        }
        return (count);
    }

    public static void calculate_Btw_n_HarmClos(MapGraph<StationVertex, TrackWeight> g, StationVertex vOrig, ResponseDto r){
        StationVertex               vTo;
        StationVertex               vFrom;
        List<StationVertex>         listOfNodes = g.vertices();

        r.betweenness = 0;
        int     manyNodes = g.numVertices();
        double  harmClos = 0;
        double  betweenNessTemp = 0;
        List<LinkedList<StationVertex>>  allPaths = getALLShortestPaths(g);

        System.out.println(allPaths.size());
        for (int i = 0; i < manyNodes; i++)
        {
            vFrom = listOfNodes.get(i);
            if (vFrom.equals(vOrig))
                continue;

            // calculation of harmonicClosines
            double dist = containsPath(g, allPaths, vOrig, vFrom);
            System.out.println("dist is: " +  1 / dist);
            if (dist != 0)
                harmClos += 1 / dist;
        }

        // calculation of betweeness
        for (int j = 0; j < allPaths.size(); j++)
        {
            LinkedList<StationVertex> currPath = allPaths.get(j);
            if (!currPath.getFirst().equals(vOrig) &&
                    !currPath.getLast().equals(vOrig) &&
                    hasThisStationInPath(currPath, vOrig))
            {
                betweenNessTemp++;
            }
        }

        if (!allPaths.isEmpty() && countManyPathsWithoutOriginInStartOrEnd(allPaths, vOrig) != 0)
            r.betweenness = betweenNessTemp / countManyPathsWithoutOriginInStartOrEnd(allPaths, vOrig);
        r.harmonicCloseness = harmClos;
    }

    public static double calculate_hubScore(ResponseDto responseDto) {
        double returnVal = responseDto.betweenness  * 0.35
                            + responseDto.harmonicCloseness * 0.35
                            + responseDto.strength * 0.3 ;
        return (returnVal);
    }

    public static ResponseDto getCentralityAnalysisForStation(MapGraph<StationVertex, TrackWeight> g, StationVertex vOrig){
        if (g == null || g.numVertices() == 0 || vOrig == null || !g.validVertex(vOrig))
            return null;

        ResponseDto r = new ResponseDto();
        r.stationId = vOrig.getStation().getId();
        r.stationName = vOrig.getStation().getName();
        r.degree = g.adjVertices(vOrig).size();
        if (g.numVertices() == 1)
        {
            r.harmonicCloseness = 1;
            r.betweenness = 1; // because it is in the path of all shortest ways(there is none)!
            r.hubScore = 1;
            r.strength = 1;
        }
        else
        {
            calculate_Btw_n_HarmClos(g, vOrig, r);
            if ((g.numVertices() - 1) == 0)
                r.strength = 1;
            else
                r.strength = (double) r.degree / (g.numVertices() - 1);
            r.hubScore = calculate_hubScore(r);
        }
        return r;
    }
}
