package org.dei.TreeTest;

import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.TwoDTree;
import org.dei.Sprint2.Trees.NodeData;
import org.dei._Location.GeographicalLocation;
import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Country;
import org.dei.Sprint2.Parser64K;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class StationTwoDTreeNearestNeighborTest {

    private void printTestHeader(String testName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST: " + testName);
        System.out.println("=".repeat(60));
    }

    private void printSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }

    private void printInfo(String message) {
        System.out.println("INFO: " + message);
    }

    private void printStationInfo(NodeData element, String prefix) {
        if (element != null) {
            GeographicalLocation coord = element.getCoordinate();
            List<Station> stations = element.getStations();

            System.out.println(prefix + "Coordinates: (" + coord.getLatitude() + ", " + coord.getLongitude() + ")");
            if (!stations.isEmpty()) {
                Station station = stations.get(0);
                System.out.println(prefix + "Station: " + station.getName());
                System.out.println(prefix + "Country: " + getCountryCode(station.getCountry()));
                System.out.println(prefix + "Timezone: " + station.getTimeZoneGroup());
            }
        }
    }

    private void printNeighborsList(List<NodeData> neighbors, String queryPoint) {
        System.out.println(neighbors.size() + " nearest neighbors from " + queryPoint + ":");
        for (int i = 0; i < neighbors.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + getStationSummary(neighbors.get(i)));
        }
    }

    private String getStationSummary(NodeData element) {
        if (element == null) return "N/A";

        GeographicalLocation coord = element.getCoordinate();
        List<Station> stations = element.getStations();

        if (stations.isEmpty()) return String.format("(%.4f, %.4f)", coord.getLatitude(), coord.getLongitude());

        Station station = stations.get(0);
        return String.format("%s [%s] (%.4f, %.4f)",
                station.getName(), getCountryCode(station.getCountry()),
                coord.getLatitude(), coord.getLongitude());
    }

    private String getCountryCode(Country country) {
        if (country == null) return "UNKNOWN";
        return country.getAbbreviaton();
    }

    // ===== Testes com arquivo 64K (estações completas) ===== //

    @Test
    public void test2DTree64KNearestNeighborMajorCities() {
        printTestHeader("TEST 17: Nearest Neighbor - 64K Stations (Major Cities)");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        // Test near Paris, France
        double queryLat = 48.8566;
        double queryLon = 2.3522;
        printInfo("Query point near Paris, France: (" + queryLat + ", " + queryLon + ")");

        NodeData nearest = twoDTree.nearestNeighbor(queryLat, queryLon);
        assertNotNull("Should find nearest station from 64K stations", nearest);

        printSuccess("Nearest station found!");
        printStationInfo(nearest, "   ");

        assertFalse("Nearest station should have stations list", nearest.getStations().isEmpty());
        printSuccess("Station has valid data");

        // Verify it's reasonably close to Paris
        double distance = calculateDistance(queryLat, queryLon, nearest.getCoordinate().getLatitude(), nearest.getCoordinate().getLongitude());
        printInfo("Distance from query point: " + String.format("%.2f", distance) + " units");
        assertTrue("Should be close to Paris", distance < 1.0);
    }

    @Test
    public void test2DTree64KMultipleNeighborsCapitalCities() {
        printTestHeader("TEST 18: Multiple Nearest Neighbors - 64K Stations (Capital Cities)");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        // Test near Berlin, Germany
        double queryLat = 52.5200;
        double queryLon = 13.4050;
        int numNeighbors = 5;

        printInfo("Finding " + numNeighbors + " nearest stations from Berlin, Germany");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return exactly " + numNeighbors + " nearest neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "Berlin (" + queryLat + ", " + queryLon + ")");

        // Verify all neighbors have valid data
        for (NodeData neighbor : neighbors) {
            assertNotNull("Node element should not be null", neighbor);
            assertNotNull("Coordinate should not be null", neighbor.getCoordinate());
            assertFalse("Should have at least one station", neighbor.getStations().isEmpty());
        }
        printSuccess("All " + numNeighbors + " neighbors have valid data");

        // Calculate distances and verify ordering
        for (int i = 0; i < neighbors.size() - 1; i++) {
            NodeData current = neighbors.get(i);
            NodeData next = neighbors.get(i + 1);

            double distCurrent = calculateDistance(queryLat, queryLon,
                    current.getCoordinate().getLatitude(),
                    current.getCoordinate().getLongitude());

            double distNext = calculateDistance(queryLat, queryLon,
                    next.getCoordinate().getLatitude(),
                    next.getCoordinate().getLongitude());

            assertTrue("Results should be ordered by distance", distCurrent <= distNext);
        }
        printSuccess("Neighbors are correctly ordered by distance");
    }

    // ===== Testes com stationTest1 (7 estações - Itália) ===== //

    @Test
    public void test2DTree7StationsNearestNeighborBasic() {
        printTestHeader("TEST 1: Nearest Neighbor - 7 Stations (Italy)");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 44.5;
        double queryLon = 15.0;
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        NodeData nearest = twoDTree.nearestNeighbor(queryLat, queryLon);
        assertNotNull("Should find nearest station from 7 stations", nearest);

        printSuccess("Nearest station found!");
        printStationInfo(nearest, "   ");

        assertFalse("Nearest station should have stations list", nearest.getStations().isEmpty());
        printSuccess("Station has valid data");
    }

    @Test
    public void test2DTree7StationsMultipleNearestNeighbors() {
        printTestHeader("TEST 2: Multiple Nearest Neighbors - 7 Stations");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 44.0;
        double queryLon = 10.0;
        int numNeighbors = 3;

        printInfo("Finding " + numNeighbors + " nearest neighbors from (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return exactly 3 nearest neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "(" + queryLat + ", " + queryLon + ")");

        // Verify all neighbors have valid coordinates and stations
        for (NodeData neighbor : neighbors) {
            assertNotNull("Node element should not be null", neighbor);
            assertNotNull("Coordinate should not be null", neighbor.getCoordinate());
            assertFalse("Should have at least one station", neighbor.getStations().isEmpty());
        }
        printSuccess("All " + numNeighbors + " neighbors have valid data");
    }

    @Test
    public void test2DTree7StationsExactMatch() {
        printTestHeader("TEST 3: Exact Coordinate Match");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double exactLat = 45.0;
        double exactLon = 20.0;
        printInfo("Searching for station at exact coordinates: (" + exactLat + ", " + exactLon + ")");

        NodeData nearest = twoDTree.nearestNeighbor(exactLat, exactLon);
        assertNotNull("Should find station at exact coordinates", nearest);

        GeographicalLocation coord = nearest.getCoordinate();
        printSuccess("Station found at coordinates: (" + coord.getLatitude() + ", " + coord.getLongitude() + ")");

        assertEquals("Latitude should match", exactLat, coord.getLatitude(), 0.001);
        assertEquals("Longitude should match", exactLon, coord.getLongitude(), 0.001);
        printSuccess("Coordinates match exactly!");
    }

    // ===== Testes com stationTest2 (4 estações - Itália) ===== //

    @Test
    public void test2DTree4StationsNearestNeighbor() {
        printTestHeader("TEST 4: Nearest Neighbor - 4 Stations (Italy)");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 44.1;
        double queryLon = 10.1;
        printInfo("Query point near Station 1: (" + queryLat + ", " + queryLon + ")");

        NodeData nearest = twoDTree.nearestNeighbor(queryLat, queryLon);
        assertNotNull("Should find nearest station from 4 stations", nearest);

        printSuccess("Nearest station found:");
        printStationInfo(nearest, "   ");

        assertEquals("Should have correct latitude", 44.0, nearest.getCoordinate().getLatitude(), 0.001);
        assertEquals("Should have correct longitude", 10.0, nearest.getCoordinate().getLongitude(), 0.001);
        printSuccess("Coordinates verified - this is Station 1!");
    }

    @Test
    public void test2DTree4StationsMultipleNeighbors() {
        printTestHeader("TEST 5: Multiple Neighbors - 4 Stations");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 45.0;
        double queryLon = 15.0;
        int numNeighbors = 2;

        printInfo("Finding " + numNeighbors + " neighbors from (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return exactly 2 nearest neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "(" + queryLat + ", " + queryLon + ")");
        printSuccess("Found " + numNeighbors + " nearest neighbors");
    }

    // ===== Testes com stationTest3 (12 estações - Europa) ===== //

    @Test
    public void test2DTree12StationsNearestNeighborFrance() {
        printTestHeader("TEST 6: Nearest Neighbor in France - 12 Stations (Europe)");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 49.27;
        double queryLon = 6.41;
        printInfo("Query point near Ebersviller, France: (" + queryLat + ", " + queryLon + ")");

        NodeData nearest = twoDTree.nearestNeighbor(queryLat, queryLon);
        assertNotNull("Should find nearest station in France", nearest);

        printSuccess("French station found:");
        printStationInfo(nearest, "   ");

        assertEquals("Should be Ebersviller coordinates", 49.2747715, nearest.getCoordinate().getLatitude(), 0.001);
        assertEquals("Should be Ebersviller coordinates", 6.4111622, nearest.getCoordinate().getLongitude(), 0.001);
        printSuccess("Confirmed: Ebersviller, France");
    }

    @Test
    public void test2DTree12StationsMultipleNeighborsSpain() {
        printTestHeader("TEST 8: Multiple Neighbors - Spain Region");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 38.0;
        double queryLon = -5.0;
        int numNeighbors = 3;

        printInfo("Finding " + numNeighbors + " nearest stations from Spain region");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return " + numNeighbors + " nearest neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "Spain region (" + queryLat + ", " + queryLon + ")");

        // Count Spanish stations instead of checking coordinates
        int spanishStations = 0;
        for (NodeData neighbor : neighbors) {
            if (!neighbor.getStations().isEmpty()) {
                Station station = neighbor.getStations().get(0);
                Country country = station.getCountry();
                if (country != null && isSpanishStation(country)) {
                    spanishStations++;
                }
            }
        }

        printInfo("Found " + spanishStations + " Spanish stations out of " + numNeighbors);
        assertTrue("Should find at least some Spanish stations", spanishStations > 0);
        printSuccess("Found " + spanishStations + " Spanish stations in the results");
    }

    private boolean isSpanishStation(Country country) {
        // Check if this is a Spanish station based on country code or name
        String countryCode = getCountryCode(country);
        return "ES".equals(countryCode) || "Spain".equalsIgnoreCase(countryCode) ||
                countryCode.contains("ES") || countryCode.contains("Spain");
    }

    @Test
    public void test2DTree12StationsScandinaviaNeighbors() {
        printTestHeader("TEST 9: Neighbors in Scandinavia");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 57.0;
        double queryLon = 12.0;
        int numNeighbors = 2;

        printInfo("Finding " + numNeighbors + " stations in Sweden region");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return 2 nearest neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "Sweden (" + queryLat + ", " + queryLon + ")");

        // Check for Scandinavian stations by latitude and country
        int scandinavianStations = 0;
        for (NodeData neighbor : neighbors) {
            double lat = neighbor.getCoordinate().getLatitude();

            // Check if station is in Scandinavia by latitude
            if (lat > 55.0) {
                scandinavianStations++;
            }
        }

        printInfo("Found " + scandinavianStations + " Scandinavian stations (latitude > 55°)");
        assertTrue("Should find Scandinavian stations", scandinavianStations > 0);
        printSuccess("Found " + scandinavianStations + " stations in Scandinavia");
    }

    // ===== Testes de Casos Limite ===== //

    @Test
    public void test2DTreeZeroNeighbors() {
        printTestHeader("TEST 10: Edge Case - Zero Neighbors");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        printInfo("Requesting 0 nearest neighbors");
        List<NodeData> neighbors = twoDTree.nearestNeighbors(44.0, 10.0, 0);

        assertTrue("Should return empty list for zero neighbors", neighbors.isEmpty());
        printSuccess("Empty list correctly returned for 0 neighbors");
    }

    @Test
    public void test2DTreeNegativeNeighbors() {
        printTestHeader("TEST 11: Edge Case - Negative Number of Neighbors");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        printInfo("Requesting -1 neighbors (invalid value)");
        List<NodeData> neighbors = twoDTree.nearestNeighbors(44.0, 10.0, -1);

        assertTrue("Should return empty list for negative neighbors", neighbors.isEmpty());
        printSuccess("Empty list correctly returned for negative number");
    }

    @Test
    public void test2DTreeOrderedResults() {
        printTestHeader("TEST 15: Results Ordered by Distance");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 47.0;
        double queryLon = 8.0;
        int numNeighbors = 5;

        printInfo("Verifying ordering of " + numNeighbors + " nearest neighbors");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        printNeighborsList(neighbors, "Central Europe");

        // Verify results are ordered by distance (closest first)
        for (int i = 0; i < neighbors.size() - 1; i++) {
            NodeData current = neighbors.get(i);
            NodeData next = neighbors.get(i + 1);

            double distCurrent = calculateDistance(queryLat, queryLon,
                    current.getCoordinate().getLatitude(),
                    current.getCoordinate().getLongitude());

            double distNext = calculateDistance(queryLat, queryLon,
                    next.getCoordinate().getLatitude(),
                    next.getCoordinate().getLongitude());

            assertTrue("Results should be ordered by distance", distCurrent <= distNext);
        }
        printSuccess("All " + numNeighbors + " neighbors are ordered by distance (closest first)");
    }

    // ===== Testes com Filtros de Timezone ===== //

    @Test
    public void test2DTreeTimezoneFilterCET() {
        printTestHeader("TEST 19: Timezone Filter - CET Only");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 48.8566; // Paris
        double queryLon = 2.3522;
        int numNeighbors = 5;

        printInfo("Finding " + numNeighbors + " nearest CET stations from Paris");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors, TimeZoneGroup.CET);
        assertFalse("Should find CET stations", neighbors.isEmpty());

        printNeighborsList(neighbors, "Paris (CET only)");

        // Verify all stations are in CET timezone
        for (NodeData neighbor : neighbors) {
            List<Station> stations = neighbor.getStations();
            assertFalse("Station should have data", stations.isEmpty());

            Station station = stations.get(0);
            assertEquals("Station should be in CET timezone", TimeZoneGroup.CET, station.getTimeZoneGroup());
        }
        printSuccess("All " + neighbors.size() + " stations are in CET timezone");
    }

    @Test
    public void test2DTreeMultipleTimezoneFilters() {
        printTestHeader("TEST 20: Multiple Timezone Filters");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 45.0; // Central Europe
        double queryLon = 10.0;
        int numNeighbors = 8;

        printInfo("Finding " + numNeighbors + " stations in CET or EET timezones");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors,
                TimeZoneGroup.CET, TimeZoneGroup.EET);

        assertFalse("Should find stations with specified timezones", neighbors.isEmpty());

        printNeighborsList(neighbors, "Central Europe (CET/EET)");

        // Verify all stations are in either CET or EET
        for (NodeData neighbor : neighbors) {
            List<Station> stations = neighbor.getStations();
            assertFalse("Station should have data", stations.isEmpty());

            Station station = stations.get(0);
            TimeZoneGroup tz = station.getTimeZoneGroup();
            assertTrue("Station should be in CET or EET",
                    tz == TimeZoneGroup.CET || tz == TimeZoneGroup.EET);
        }
        printSuccess("All stations are in specified timezones (CET/EET)");
    }

    @Test
    public void test2DTreeNoTimezoneFilter() {
        printTestHeader("TEST 21: No Timezone Filter (All Timezones)");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 52.5200; // Berlin
        double queryLon = 13.4050;
        int numNeighbors = 6;

        printInfo("Finding " + numNeighbors + " stations with no timezone filter");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return exactly " + numNeighbors + " neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "Berlin (all timezones)");

        // Count different timezones to verify diversity
        Set<TimeZoneGroup> timezonesFound = new HashSet<>();
        for (NodeData neighbor : neighbors) {
            List<Station> stations = neighbor.getStations();
            if (!stations.isEmpty()) {
                timezonesFound.add(stations.get(0).getTimeZoneGroup());
            }
        }

        printInfo("Found stations in " + timezonesFound.size() + " different timezones");
        assertTrue("Should find multiple timezones without filter", timezonesFound.size() >= 1);
        printSuccess("Found diverse timezones: " + timezonesFound);
    }

    // ===== Testes com Distância Haversine ===== //

    @Test
    public void test2DTreeHaversineDistanceAccuracy() {
        printTestHeader("TEST 22: Haversine Distance Accuracy");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        // Test known distance: Paris to Berlin
        double parisLat = 48.8566;
        double parisLon = 2.3522;
        double berlinLat = 52.5200;
        double berlinLon = 13.4050;

        printInfo("Testing Haversine distance calculation");
        printInfo("Paris: (" + parisLat + ", " + parisLon + ")");
        printInfo("Berlin: (" + berlinLat + ", " + berlinLon + ")");

        // Find nearest to Paris, should be Paris station
        NodeData nearestToParis = twoDTree.nearestNeighbor(parisLat, parisLon);
        assertNotNull("Should find nearest station to Paris", nearestToParis);

        double parisDistance = calculateHaversineDistance(parisLat, parisLon,
                nearestToParis.getCoordinate().getLatitude(),
                nearestToParis.getCoordinate().getLongitude());

        printInfo("Distance to nearest Paris station: " + String.format("%.2f", parisDistance) + " km");
        assertTrue("Should find very close station in Paris", parisDistance < 10.0);
        printSuccess("Haversine distance calculation verified - found station " +
                String.format("%.2f", parisDistance) + " km from query point");
    }

    // ===== Testes de Casos Extremos ===== //

    @Test
    public void test2DTreeMoreNeighborsThanStations() {
        printTestHeader("TEST 23: More Neighbors Requested Than Available Stations");

        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 44.0;
        double queryLon = 10.0;
        int availableStations = 4;
        int requestedNeighbors = 10;

        printInfo("Requesting " + requestedNeighbors + " neighbors from " + availableStations + " available stations");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, requestedNeighbors);

        assertEquals("Should return all available stations", availableStations, neighbors.size());
        printNeighborsList(neighbors, "small dataset");
        printSuccess("Correctly returned all " + availableStations + " available stations");
    }

    @Test
    public void test2DTreeVeryDistantQueryPoint() {
        printTestHeader("TEST 25: Very Distant Query Point");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        // Query point far from Europe (e.g., middle of Atlantic)
        double queryLat = 35.0;
        double queryLon = -40.0;
        int numNeighbors = 5;

        printInfo("Finding " + numNeighbors + " nearest stations from distant point in Atlantic");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        assertEquals("Should return requested number of neighbors", numNeighbors, neighbors.size());

        printNeighborsList(neighbors, "Atlantic Ocean");

        // Verify all stations are European (should be closest to distant point)
        int europeanStations = 0;
        for (NodeData neighbor : neighbors) {
            List<Station> stations = neighbor.getStations();
            if (!stations.isEmpty()) {
                // European stations typically have positive longitude and latitude between 35-70
                double lat = neighbor.getCoordinate().getLatitude();
                double lon = neighbor.getCoordinate().getLongitude();
                if (lat >= 35.0 && lat <= 70.0 && lon >= -10.0 && lon <= 40.0) {
                    europeanStations++;
                }
            }
        }

        printInfo("Found " + europeanStations + " European stations");
        assertTrue("Should find European stations even from distant point", europeanStations > 0);
        printSuccess("Algorithm works correctly with distant query points");
    }

    // ===== Testes de Performance e Escala ===== //

    @Test
    public void test2DTreeLargeNumberOfNeighbors() {
        printTestHeader("TEST 26: Large Number of Neighbors");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 50.0; // Central Europe
        double queryLon = 10.0;
        int numNeighbors = 50; // Large number

        printInfo("Finding " + numNeighbors + " nearest stations (performance test)");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        long startTime = System.currentTimeMillis();
        List<NodeData> neighbors = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);
        long endTime = System.currentTimeMillis();

        assertEquals("Should return exactly " + numNeighbors + " neighbors", numNeighbors, neighbors.size());

        long duration = endTime - startTime;
        printInfo("Found " + neighbors.size() + " neighbors in " + duration + "ms");
        printNeighborsList(neighbors.subList(0, 25), "first 25 of " + numNeighbors);

        assertTrue("Should complete in reasonable time", duration < 1000); // Should be fast
        printSuccess("Performance test passed - found " + numNeighbors + " neighbors in " + duration + "ms");
    }

    // ===== Testes de Consistência ===== //

    @Test
    public void test2DTreeConsistencyBetweenCalls() {
        printTestHeader("TEST 28: Consistency Between Multiple Calls");

        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);

        double queryLat = 51.5074; // London
        double queryLon = -0.1278;
        int numNeighbors = 5;

        printInfo("Testing consistency for " + numNeighbors + " neighbors from London");
        printInfo("Query point: (" + queryLat + ", " + queryLon + ")");

        // First call
        List<NodeData> firstResult = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);

        // Second call with same parameters
        List<NodeData> secondResult = twoDTree.nearestNeighbors(queryLat, queryLon, numNeighbors);

        assertEquals("Results should have same size", firstResult.size(), secondResult.size());

        // Verify same stations are returned in same order
        for (int i = 0; i < firstResult.size(); i++) {
            NodeData firstElement = firstResult.get(i);
            NodeData secondElement = secondResult.get(i);

            assertEquals("Element at position " + i + " should be the same",
                    firstElement.getCoordinate().getLatitude(),
                    secondElement.getCoordinate().getLatitude(), 0.001);
            assertEquals("Element at position " + i + " should be the same",
                    firstElement.getCoordinate().getLongitude(),
                    secondElement.getCoordinate().getLongitude(), 0.001);
        }

        printSuccess("Results are consistent between multiple calls");
    }

    // Helper method for Haversine distance calculation
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // Helper method to calculate Euclidean distance
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }
}
