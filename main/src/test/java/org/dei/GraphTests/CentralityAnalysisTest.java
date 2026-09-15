package org.dei.GraphTests;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint3.GraphAlgorithms.CentralityAnalysis;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei.Sprint3.Graph.CommonGraph;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.CartesianCoordinates;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertNull;


public class CentralityAnalysisTest {
    private static final String stationsTestFile = "src/main/resources/ Belgian_Rail_Network/stations.csv";
    private static final String linesTestFile = "src/main/resources/ Belgian_Rail_Network/lines.csv";

    public Station createStation(){
        Country country = new Country("BE");
        TimeZone timeZone = new TimeZone("Europe/Brussels");
        TimeZoneGroup timeZoneGroup = TimeZoneGroup.CET;
        GeographicalLocation location = new GeographicalLocation(10,10);

        Station station = new Station(
                location,
                timeZoneGroup,
                timeZone, country,"station1", 1,
                false,
                false,
                false
        );
        return (station);
    }

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

    public StationVertex createStationsAndPopulateGraphV1connectedToEveryone(Graph<StationVertex, TrackWeight> g){
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
        g.addEdge(vertex1, vertex3, new TrackWeight(10, 10, 10));
        g.addEdge(vertex1, vertex4, new TrackWeight(10, 10, 10));
        g.addEdge(vertex1, vertex5, new TrackWeight(10, 10, 10));
        g.addEdge(vertex1, vertex6, new TrackWeight(10, 10, 10));
        g.addEdge(vertex1, vertex7, new TrackWeight(10, 10, 10));
        g.addEdge(vertex1, vertex8, new TrackWeight(10, 10, 10));

        return (vertex1);
    }

    public StationVertex createStationsAndPopulateGraphOneIsolated(Graph<StationVertex, TrackWeight> g){
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

        g.addEdge(vertex2, vertex5, new TrackWeight(10, 10, 10));
        g.addEdge(vertex5, vertex3, new TrackWeight(10, 10, 10));
        g.addEdge(vertex3, vertex6, new TrackWeight(10, 10, 10));
        g.addEdge(vertex7, vertex6, new TrackWeight(10, 10, 10));
        g.addEdge(vertex4, vertex8, new TrackWeight(10, 10, 10));
        g.addEdge(vertex7, vertex8, new TrackWeight(10, 10, 10));
        g.addEdge(vertex3, vertex4, new TrackWeight(10, 10, 10));

        return (vertex1);
    }

    public StationVertex createOneStationAndPopulateGraph(Graph<StationVertex, TrackWeight> g){
        Station to_return = createStationCustom("station1", 1, 10, 10);
        StationVertex vertex1 = new StationVertex(new CartesianCoordinates(10, 10), to_return);

        g.addVertex(vertex1);
        return (vertex1);
    }

    public StationVertex createTwoStationAndPopulateGraphNotConnected(Graph<StationVertex, TrackWeight> g){
        Station to_return = createStationCustom("station1", 1, 10, 10);
        StationVertex vertex1 = new StationVertex(new CartesianCoordinates(10, 10), to_return);
        StationVertex vertex2 = new StationVertex(new CartesianCoordinates(11, 11),
                createStationCustom("station2", 2, 11, 11));

        g.addVertex(vertex1);
        g.addVertex(vertex2);
        return (vertex1);
    }

    public StationVertex createTwoStationAndPopulateGraphConnected(Graph<StationVertex, TrackWeight> g){
        Station to_return = createStationCustom("station1", 1, 10, 10);
        StationVertex vertex1 = new StationVertex(new CartesianCoordinates(10, 10), to_return);
        StationVertex vertex2 = new StationVertex(new CartesianCoordinates(11, 11),
                createStationCustom("station2", 2, 11, 11));

        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addEdge(vertex1, vertex2, new TrackWeight(10, 10, 10));
        return (vertex1);
    }

    public void printDto(CentralityAnalysis.ResponseDto d) {
        System.out.println("stationId: " + d.stationId);
        System.out.println("stationName: " + d.stationName);
        System.out.println("degree: " + d.degree);
        System.out.println("strength: " + d.strength);
        System.out.println("Betweenness: " + d.betweenness);
        System.out.println("Harmonic: " + d.harmonicCloseness);
        System.out.println("hubScore: " + d.hubScore);
    }


    // Tests to be developed

    // Invalids:
    // 1 - test with graph null
    // 2 - test with origin vertex null
    // 3 - test with origin vertex not in graph
    @Test
    void testNullGraph() {
        ///  Arrange
        MapGraph graph = null;
        StationVertex vertex = null;

        /// Act & Assert
        assertNull(CentralityAnalysis.getCentralityAnalysisForStation(graph, vertex));
    }

    @Test
    void testNullVertexGraph() {
        ///  Arrange
        MapGraph graph = new MapGraph<>(false);
        StationVertex vertex = null;

        /// Act & Assert
        assertNull(CentralityAnalysis.getCentralityAnalysisForStation(graph, vertex));
    }

    @Test
    void testVertexNotInEmptyGraph() {
        ///  Arrange
        MapGraph graph = new MapGraph<>(false);
        StationVertex vertex = new StationVertex(new CartesianCoordinates(10, 10), createStation());

        /// Act & Assert
        assertNull(CentralityAnalysis.getCentralityAnalysisForStation(graph, vertex));
    }

    @Test
    void testVertexNotInGraph() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        createStationsAndPopulateGraph(graph);

        StationVertex vertexNoINGraph = new StationVertex(new CartesianCoordinates(1, 1),
                                    createStationCustom("station30", 30, 1, 1));

        /// Act & Assert
        assertNull(CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexNoINGraph));
    }

    // Valid:
    // 1 - test with graph with only that station
    // 2 - test with graph with 2 stations connected
    // 3 - test with graph with 2 stations not connected
    // 4 - test with graph with vOrig isolated from others (no connections) (should be 0)
    // 5 - test with graph with vOrig valid with supposed value
    // 6 - test with graph with vOrig valid with another supposed value
    // 7 - test with graph with vOrig directly connected to all other stations (should be 1)

    @Test
    void testVertexOneStations() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createOneStationAndPopulateGraph(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 0);
        assert(dto.strength == 1);
        assert(dto.hubScore == 1);
        assert(dto.betweenness == 1);
        assert(dto.harmonicCloseness == 1);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexTwoStationsConnected() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createTwoStationAndPopulateGraphConnected(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 1);
        assert(dto.strength == 1);
        assert(dto.hubScore == 0.33499999999999996); // change it later
        assert(dto.betweenness == 0);
        assert(dto.harmonicCloseness == 0.1);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexTwoStationsNotConnected() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createTwoStationAndPopulateGraphNotConnected(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 0);
        assert(dto.strength == 0);
        assert(dto.hubScore == 0);
        assert(dto.betweenness == 0);
        assert(dto.harmonicCloseness == 0);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexGraphV1Isolated() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraphOneIsolated(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 0);
        assert(dto.strength == 0);
        assert(dto.hubScore == 0);
        assert(dto.betweenness == 0);
        assert(dto.harmonicCloseness == 0);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexGreatGraphV1() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraph(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 1);
        assert(dto.strength == 0.14285714285714285);
        assert(dto.hubScore == 0.1385238095238095);
        assert(dto.betweenness == 0);
        assert(dto.harmonicCloseness == 0.2733333333333333);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexGreatGraphV3() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        createStationsAndPopulateGraph(graph);
        StationVertex vertexInGraph = graph.vertex(2);
        System.out.println(vertexInGraph.getStation().getId());
        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 3);
        assert(dto.strength == 0.42857142857142855);
        assert(dto.hubScore == 0.5144047619047618);
        assert(dto.betweenness == 0.6190476190476191);
        assert(dto.harmonicCloseness == 0.4833333333333333);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    @Test
    void testVertexGraphV1connectedToAll() {
        ///  Arrange
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(false);
        StationVertex vertexInGraph = createStationsAndPopulateGraphV1connectedToEveryone(graph);

        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, vertexInGraph);

        /// Assert
        assert(dto != null);
        printDto(dto);
        assert(dto.degree == 7);
        assert(dto.strength == 1);
        assert(dto.hubScore == 0.895);
        assert(dto.betweenness == 1);
        assert(dto.harmonicCloseness == 0.7);
        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

    // Valid Tests with file:
    //

    @Test
    void testAnalysisReadingFromFile() {
        ///  Arrange
        RailNetworkGraphService graphService = new RailNetworkGraphService();
        ParserBerlgianNW parser = new ParserBerlgianNW(graphService);
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        MapGraph<StationVertex, TrackWeight> graph = graphService.getMapGraph();

        System.out.println(graph.vertices().size());
        /// Act
        CentralityAnalysis.ResponseDto dto = CentralityAnalysis.getCentralityAnalysisForStation(graph, graph.vertex(5));

        /// Assert
        //sprintDto(dto);
        assert(dto != null);
        //printDto(dto);
//        assert(dto.degree == 7);
//        //assert(dto.hubScore == 0.09);
//        assert(dto.betweenness == 1);
//        assert(dto.harmonicCloseness == 0.7);
//        assert(dto.stationName.equals(vertexInGraph.getStation().getName()));
//        assert(dto.stationId == vertexInGraph.getStation().getId());
    }

}
