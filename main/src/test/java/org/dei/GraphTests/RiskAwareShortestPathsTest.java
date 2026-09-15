package org.dei.GraphTests;

import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.dei.Sprint3.GraphAlgorithms.BellmanFordPath;
import org.dei.Sprint3.GraphAlgorithms.RiskAwareShortestPaths;
import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.RailNetworkGraphs.CartesianCoordinates;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RiskAwareShortestPathsTest {

    // for big tests with big file
    private final String belgiumStationsFile = "src/main/resources/ Belgian_Rail_Network/stations.csv";
    private final String belgiumLinesFile = "src/main/resources/ Belgian_Rail_Network/lines.csv";
    private RailNetworkGraphService graphService;
    private ParserBerlgianNW parser;

    // for small tests
    private MapGraph<StationVertex, TrackWeight> graph;
    private StationVertex A, B, C, D, E;

    @BeforeEach
    void setUp() {
        graph = new MapGraph<>(true);
    }

    void setupBelgiumRailwayNetwork(){
        graphService = new RailNetworkGraphService();
        parser = new ParserBerlgianNW(graphService);

        boolean res = parser.loadRailNetwork(belgiumStationsFile, belgiumLinesFile);
        System.out.println(res);
    }

    void normalSetup(){
        A = createVertex("A", 1, 0, 0);
        B = createVertex("B", 2, 1, 0);
        C = createVertex("C", 3, 2, 0);
        D = createVertex("D", 4, 0, 5);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);

        // A -> B -> C (best path = 5 + 3 = 8)
        graph.addEdge(A, B, new TrackWeight(5, 5, 1));
        graph.addEdge(B, C, new TrackWeight(3, 3, 1));

        // Direct but worse path A -> C = 10
        graph.addEdge(A, C, new TrackWeight(10, 10, 1));
    }

    void negativeCycleSetup(){
        A = createVertex("A", 1, 0, 0);
        B = createVertex("B", 2, 1, 0);
        C = createVertex("C", 3, 2, 0);
        D = createVertex("D", 4, 3, 0);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);

        graph.addEdge(A, B, new TrackWeight(2, 2, 1));
        graph.addEdge(B, C, new TrackWeight(-10, 2, 1));
        graph.addEdge(C, A, new TrackWeight(1, 2, 1));
        graph.addEdge(A, D, new TrackWeight(4, 5, 2));
    }

    public StationVertex createVertex(String name, int id, int x, int y) {
        Country country = new Country("BE");
        TimeZone timeZone = new TimeZone("Europe/Brussels");
        TimeZoneGroup timeZoneGroup = TimeZoneGroup.CET;
        GeographicalLocation location = new GeographicalLocation(x, y);

        Station station = new Station(
                location,
                timeZoneGroup,
                timeZone,
                country,
                name,
                id,
                false,
                false,
                false
        );

        return new StationVertex(new CartesianCoordinates(x, y), station);
    }

    // test bellman path possible
    @Test
    void testShortestPathBetweenTwoStations() {
        normalSetup();

        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, C);

        assertNotNull(result);
        assertFalse(result.hasNegativeCycle());

        List<StationVertex> path = result.getShortestPath();

        assertEquals(3, path.size());
        assertEquals(A, path.get(0));
        assertEquals(B, path.get(1));
        assertEquals(C, path.get(2));
    }

    // test total cost correct
    @Test
    void testTotalCostIsCorrect() {
        normalSetup();

        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, C);

        assertEquals(8.0, result.getTotalCost());
        assertEquals(8.0, result.getDistances().get(C));
    }

    // test orig = dest
    @Test
    void testSameOriginAndDestination() {
        normalSetup();

        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, A);

        List<StationVertex> path = result.getShortestPath();

        assertEquals(1, path.size());
        assertEquals(A, path.getFirst());
        assertEquals(0.0, result.getTotalCost());
        assertFalse(result.hasNegativeCycle());
    }

    // negative cycle detection
    @Test
    void testNegativeCycleDetection() {
        negativeCycleSetup();

        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, C);

        assertTrue(result.hasNegativeCycle());
        assertNotNull(result.getNegativeCycleEdges());
        assertFalse(result.getNegativeCycleEdges().isEmpty());
    }

    // no possible path
    @Test
    void testNoPathExists() {
        normalSetup();

        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, D);

        assertTrue(result.getShortestPath().isEmpty() || result.getTotalCost() == Double.POSITIVE_INFINITY);
    }

    @Test
    void testPathOutputNormal() {
        normalSetup();
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, C);
        RiskAwareShortestPaths.printBellmanFordPath(result);
    }

    @Test
    void testPathOutputNormalButEmpty() {
        normalSetup();
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, D);
        RiskAwareShortestPaths.printBellmanFordPath(result);
    }

    @Test
    void testPathOutputNegativeCycle() {
        negativeCycleSetup();
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(graph, A, C);
        RiskAwareShortestPaths.printBellmanFordPath(result);
    }

    @Test
    void testBelgiumPathOutputNormal() {
        setupBelgiumRailwayNetwork();

        StationVertex belgiumStationA = graphService.findStationById(67);
        StationVertex belgiumStationB = graphService.findStationById(347);
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(
                graphService.getMapGraph(),
                belgiumStationA,
                belgiumStationB
        );
        RiskAwareShortestPaths.printBellmanFordPath(result);
    }

    @Test
    void testBelgiumPathNoPath() {
        setupBelgiumRailwayNetwork();

        StationVertex belgiumStationA = graphService.findStationById(67);
        StationVertex belgiumStationB = graphService.findStationById(380);
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(
                graphService.getMapGraph(),
                belgiumStationA,
                belgiumStationB
        );

        assertEquals(Double.POSITIVE_INFINITY, result.getTotalCost());
        assertEquals(0, result.getShortestPath().size());
    }

    @Test
    void testBelgiumPathOutputNoPath() {
        setupBelgiumRailwayNetwork();

        StationVertex belgiumStationA = graphService.findStationById(67);
        StationVertex belgiumStationB = graphService.findStationById(380);
        BellmanFordPath result = RiskAwareShortestPaths.getBellmanFordPath(
                graphService.getMapGraph(),
                belgiumStationA,
                belgiumStationB
        );
        RiskAwareShortestPaths.printBellmanFordPath(result);
    }
}
