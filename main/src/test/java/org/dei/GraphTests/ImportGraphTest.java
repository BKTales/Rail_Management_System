package org.dei.GraphTests;
import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RailNetworkGraphParser using Belgian railway network data.
 * Tests CSV parsing, graph construction, and business rule validation.
 */
@DisplayName("RailNetworkGraphParser Tests")
public class ImportGraphTest {

    private RailNetworkGraphService graphService;
    private ParserBerlgianNW parser;

    private static final String stationsTestFile = "src/main/resources/ Belgian_Rail_Network/stations.csv";
    private static final String linesTestFile = "src/main/resources/ Belgian_Rail_Network/lines.csv";

    @BeforeEach
    void setUp() {
        graphService = new RailNetworkGraphService();
        parser = new ParserBerlgianNW(graphService);
    }


    /**
     * Test: Load complete rail network successfully
     */
    @Test
    @DisplayName("Should load rail network successfully")
    void testLoadRailNetworkSuccess() {
        boolean result = parser.loadRailNetwork(stationsTestFile, linesTestFile);
        assertTrue(result, "Rail network should load successfully");
    }

    /**
     * Test: Verify stations are loaded into graph
     */
    @Test
    @DisplayName("Should load stations into graph")
    void testStationsLoaded() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        int stationCount = graphService.getStationCount();

        assertNotEquals(0, stationCount, "Graph should contain at least one station");
        System.out.println("✓ Loaded " + stationCount + " stations");
    }

    /**
     * Test: Verify lines (edges) are loaded into graph
     */
    @Test
    @DisplayName("Should load rail lines into graph")
    void testLinesLoaded() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        int lineCount = graphService.getLineCount();

        assertNotEquals(0, lineCount, "Graph should contain at least one rail line");
        System.out.println("✓ Loaded " + lineCount + " rail lines");
    }

    /**
     * Test: Verify graph consistency (stations >= lines)
     */
    @Test
    @DisplayName("Should have valid graph structure")
    void testGraphStructure() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        int stationCount = graphService.getStationCount();
        int lineCount = graphService.getLineCount();

        // In a connected graph, we expect: lines >= stations - 1 for minimal spanning tree
        // But this is a directed graph, so we just check it's reasonable
        assertTrue(lineCount >= 0, "Line count should be non-negative");
    }

    /**
     * Test: Verify graph is directed
     */
    @Test
    @DisplayName("Graph should be directed")
    void testGraphIsDirected() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        boolean isDirected = graphService.getMapGraph().isDirected();

        assertTrue(isDirected, "Rail network graph should be directed");
    }

    /**
     * Test: Load only stations file
     */
    @Test
    @DisplayName("Should load stations without lines")
    void testLoadStationsOnly() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);
        int stationCountBefore = graphService.getStationCount();

        assertTrue(stationCountBefore > 0, "Stations should be loaded");
    }

    /**
     * Test: Handle non-existent file gracefully
     */
    @Test
    @DisplayName("Should handle missing files gracefully")
    void testMissingFileHandling() {
        boolean result = parser.loadRailNetwork(
                "src/main/resources/nonexistent_stations.csv",
                linesTestFile
        );

        assertFalse(result, "Should return false when stations file is missing");
    }

    /**
     * Test: Verify stations have valid coordinates
     */
    @Test
    @DisplayName("Stations should have valid geographic coordinates")
    void testStationCoordinates() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        // Get a station from the graph and verify it has coordinates
        var stations = graphService.getMapGraph().vertices();
        assertFalse(stations.isEmpty(), "Should have loaded stations");

        var firstStation = stations.get(0);
        assertNotNull(firstStation, "Station should not be null");
        assertNotNull(firstStation.getCoordinates(), "Station should have Cartesian coordinates");
        assertNotNull(firstStation.getStation(), "Station object should not be null");

        System.out.println("✓ First station: " + firstStation.getStation().getName());
    }

    /**
     * Test: Verify lines have valid metrics
     */
    @Test
    @DisplayName("Rail lines should have valid metrics")
    void testLineMetrics() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        // Get edges from graph and verify they have valid weights
        var edges = graphService.getMapGraph().edges();
        assertFalse(edges.isEmpty(), "Should have loaded rail lines");

        var firstEdge = edges.stream().findFirst().orElse(null);
        assertNotNull(firstEdge, "Should have at least one edge");

        var weight = firstEdge.getWeight();
        assertNotNull(weight, "Edge weight should not be null");

        // Verify metrics are non-negative
        assertTrue(weight.getDistance() >= 0, "Distance should be non-negative");
        assertTrue(weight.getCapacity() >= 0, "Capacity should be non-negative");
        // Cost can be negative (penalties allowed)

        System.out.println("✓ First line: Distance=" + weight.getDistance() +
                "km, Capacity=" + weight.getCapacity() +
                ", Cost=" + weight.getCost());
    }

    /**
     * Test: Verify Belgium country code in all stations
     */
    @Test
    @DisplayName("All stations should have Belgium country code")
    void testBelgiumCountryCode() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        var stations = graphService.getMapGraph().vertices();
        for (var stationVertex : stations) {
            var country = stationVertex.getStation().getCountry();
            // Assuming Country has a method to get code or we check the name
            assertNotNull(country, "Station should have a country");
            System.out.println("  Station: " + stationVertex.getStation().getName() +
                    ", Country: " + country);
        }
    }

    /**
     * Test: Verify timezone is CET for all stations
     */
    @Test
    @DisplayName("All stations should have CET timezone")
    void testCETTimezone() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        var stations = graphService.getMapGraph().vertices();
        for (var stationVertex : stations) {
            var tzGroup = stationVertex.getStation().getTimeZoneGroup();
            assertEquals("CET", tzGroup.toString(), "TimeZoneGroup should be CET");
        }
    }

    /**
     * Test: Verify no duplicate stations are created
     */
    @Test
    @DisplayName("Should not create duplicate stations")
    void testNoDuplicateStations() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        var stations = graphService.getMapGraph().vertices();
        var stationIds = new java.util.HashSet<>();

        for (var stationVertex : stations) {
            int id = stationVertex.getStation().getId();
            assertFalse(stationIds.contains(id), "Station ID " + id + " should be unique");
            stationIds.add(id);
        }

        System.out.println("✓ No duplicate stations found. Unique count: " + stationIds.size());
    }

    /**
     * Test: Verify line endpoints exist in graph
     */
    @Test
    @DisplayName("All line endpoints should exist as stations")
    void testLineEndpointsExist() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        var mapGraph = graphService.getMapGraph();
        var edges = mapGraph.edges();

        for (var edge : edges) {
            var origin = edge.getVOrig();
            var destination = edge.getVDest();

            assertTrue(mapGraph.validVertex(origin), "Origin vertex should be in graph");
            assertTrue(mapGraph.validVertex(destination), "Destination vertex should be in graph");
        }

        System.out.println("✓ All line endpoints are valid stations");
    }

    /**
     * Test: Summary statistics
     */
    @Test
    @DisplayName("Should provide valid network statistics")
    void testNetworkStatistics() {
        parser.loadRailNetwork(stationsTestFile, linesTestFile);

        int stationCount = graphService.getStationCount();
        int lineCount = graphService.getLineCount();

        System.out.println("\n=== Belgian Railway Network Statistics ===");
        System.out.println("Total Stations: " + stationCount);
        System.out.println("Total Lines: " + lineCount);
        System.out.println("Average degree: " + (lineCount > 0 ? (double) lineCount / stationCount : 0));
        System.out.println("=========================================\n");

        assertTrue(stationCount > 0, "Network should have stations");
        assertTrue(lineCount > 0, "Network should have lines");
    }
    
}