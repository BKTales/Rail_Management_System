package org.dei.GraphTests;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint3.GraphAlgorithms.CentralityAnalysis;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.CartesianCoordinates;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertFalse;

public class BellmanFordTest
{
    public Station createStationCustom(String name, int id, int x, int y){
        Country country = new Country("BE");
        TimeZone timeZone = new TimeZone("Europe/Brussels");
        TimeZoneGroup timeZoneGroup = TimeZoneGroup.CET;
        GeographicalLocation location = new GeographicalLocation(x,y);

        Station station = new Station(
                location,
                timeZoneGroup,
                timeZone, country,name, id,
                false,
                false,
                false
        );
        return (station);
    }

    public StationVertex createSmallGraphWithNegativeCycle(Graph<StationVertex, TrackWeight> g) {
        // Create vertices
        StationVertex A = new StationVertex(new CartesianCoordinates(0, 0), createStationCustom("A", 1, 0, 0));
        StationVertex B = new StationVertex(new CartesianCoordinates(1, 0), createStationCustom("B", 2, 1, 0));
        StationVertex C = new StationVertex(new CartesianCoordinates(2, 0), createStationCustom("C", 3, 2, 0));
        StationVertex D = new StationVertex(new CartesianCoordinates(3, 0), createStationCustom("D", 4, 3, 0));

        // Add vertices to the graph
        g.addVertex(A);
        g.addVertex(B);
        g.addVertex(C);
        g.addVertex(D);

        // Add edges
        g.addEdge(A, B, new TrackWeight(1, 1, 1));    // weight = 1
        g.addEdge(B, C, new TrackWeight(2, 2, 2));    // weight = 2
        g.addEdge(C, A, new TrackWeight(-5, -5, -5)); // weight = -5 (negative cycle)
        g.addEdge(C, D, new TrackWeight(1, 1, 1));    // weight = 1

        return A; // source vertex
    }

    public StationVertex createStationsAndPopulateGraph(Graph<StationVertex, TrackWeight> g){
        Station to_return = createStationCustom("station1", 1, 10, 10);
        StationVertex vertex1 = new StationVertex(new CartesianCoordinates(10, 10), to_return);
        StationVertex vertex2 = new StationVertex(new CartesianCoordinates(11, 11),
                createStationCustom("station2", 2, 11, 11));
        StationVertex vertex3 = new StationVertex(new CartesianCoordinates(12, 9),
                createStationCustom("station3", 3, 12, 9));
        StationVertex vertex4 = new StationVertex(new CartesianCoordinates(9, 12),
                createStationCustom("station4", 4, 9, 12));
        StationVertex vertex5 = new StationVertex(new CartesianCoordinates(15, 10),
                createStationCustom("station5", 5, 15, 10));
        StationVertex vertex6 = new StationVertex(new CartesianCoordinates(10, 15),
                createStationCustom("station6", 6, 10, 15));
        StationVertex vertex7 = new StationVertex(new CartesianCoordinates(14, 14),
                createStationCustom("station7", 7, 14, 14));
        StationVertex vertex8 = new StationVertex(new CartesianCoordinates(8, 8),
                createStationCustom("station8", 8, 8, 8));

        // Add all vertices to the graph
        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addVertex(vertex3);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addVertex(vertex6);
        g.addVertex(vertex7);
        g.addVertex(vertex8);

        g.addEdge(vertex1, vertex2, new TrackWeight(10, 10, 10));
        g.addEdge(vertex2, vertex5, new TrackWeight(10, 10, 10));
        g.addEdge(vertex5, vertex3, new TrackWeight(10, 10, 10));
        g.addEdge(vertex3, vertex6, new TrackWeight(10, 10, 10));
        g.addEdge(vertex7, vertex6, new TrackWeight(10, 10, 10));
        g.addEdge(vertex4, vertex8, new TrackWeight(10, 10, 10));
        g.addEdge(vertex7, vertex8, new TrackWeight(10, 10, 10));
        g.addEdge(vertex3, vertex4, new TrackWeight(10, 10, 10));
        return (vertex1);
    }

    @Test
    public void testBellmanFord() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraph(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, vertexInGraph, graph.vertex(2), shortestPath);

        /// Assert
        assert (value);
    }

    @Test
    public void testBellmanFordNegativeCycleInvalid() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createSmallGraphWithNegativeCycle(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, graph.vertex(2), vertexInGraph, shortestPath);

        System.out.println("Path between: " + vertexInGraph.getStation().getName() + " and " + graph.vertex(2).getStation().getName());
        for (StationVertex vertex : shortestPath) {
            System.out.println(vertex.getStation().getName());
        }
        /// Assert
        assertFalse (value);
        assert (shortestPath.isEmpty());
    }

    @Test
    public void testBellmanFordNegativeCycleInvalid2() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createSmallGraphWithNegativeCycle(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, vertexInGraph, graph.vertex(1), shortestPath);

        /// Assert
        assertFalse (value);
        assert (shortestPath.isEmpty());
    }

    @Test
    public void testBellmanFordNegativeCycleInvalid3() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createSmallGraphWithNegativeCycle(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, graph.vertex(2), graph.vertex(3), shortestPath);

        /// Assert
        assertFalse (value);
        assert (shortestPath.isEmpty());
    }

    @Test
    public void testBellmanFordNegativeCycleValid() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraph(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, vertexInGraph, graph.vertex(2), shortestPath);


        System.out.println("Path between: " + vertexInGraph.getStation().getName() + " and " + graph.vertex(1).getStation().getName());
        for (StationVertex vertex : shortestPath)
            System.out.println(vertex.getStation().getName());

        /// Assert
        assert (value);
        assertFalse (shortestPath.isEmpty());
        assert (shortestPath.get(0).equals(vertexInGraph));              // V1
        assert (shortestPath.get(1).equals(graph.vertex(1)));       // V2
        assert (shortestPath.get(2).equals(graph.vertex(4)));       // V5
        assert (shortestPath.get(3).equals(graph.vertex(2)));       // V3
    }

    @Test
    public void testBellmanFordNegativeCycleValid2() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraph(graph);
        LinkedList<StationVertex> shortestPath = new LinkedList<>();

        /// Act
        boolean value = CentralityAnalysis.bellmanFord(graph, graph.vertex(2), graph.vertex(7), shortestPath);


        System.out.println("Path between: " + graph.vertex(2).getStation().getName() + " and " + graph.vertex(7).getStation().getName());
        for (StationVertex vertex : shortestPath)
            System.out.println(vertex.getStation().getName());

        /// Assert
        assert (value);
        assertFalse (shortestPath.isEmpty());
        assert (shortestPath.get(0).equals(graph.vertex(2)));       // V3
        assert (shortestPath.get(1).equals(graph.vertex(3)));       // V4
        assert (shortestPath.get(2).equals(graph.vertex(7)));       // V8
    }
}