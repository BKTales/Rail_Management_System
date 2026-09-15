package org.dei.TreeTest;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.NodeData;
import org.dei.Sprint2.Trees.PrintTree;

import org.junit.Test;
import org.dei.Sprint2.Parser64K;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class StationAVL {

    // ===== Test Related to the creation and insert on Station Avl ===== //
    @Test
    public void testInsertStations() {
        TimeZoneGroup tzGroup = TimeZoneGroup.CET;
        TimeZone tz = new TimeZone("Europe/Rome");
        Country country = new Country("IT");

        GeographicalLocation[] locations = {
                new GeographicalLocation(44, 10),
                new GeographicalLocation(43, 20),
                new GeographicalLocation(44, 10),
                new GeographicalLocation(46, 12),
                new GeographicalLocation(46, 10)
        };

        Station[] stations = {
                new Station(locations[0], tzGroup, tz, country, "Rome", 1, true, false, false),
                new Station(locations[1], tzGroup, tz, country, "Milan", 2, true, false, false),
                new Station(locations[2], tzGroup, tz, country, "Roma2", 3, true, false, false),
                new Station(locations[3], tzGroup, tz, country, "Venice", 4, true, false, false),
                new Station(locations[4], tzGroup, tz, country, "Turin", 5, true, false, false)
        };

        AVL avl = new AVL();

        for (int i = 0; i < stations.length; i++) {
            GeographicalLocation loc = locations[i];
            Station sta = stations[i];
            NodeData node = avl.findNodeByCoordinates(loc);
            if (node != null) {
                node.addStation(sta);
            } else {
                avl.insert(new NodeData(loc, sta));
            }
        }

        assertEquals("The number of nodes should be 4", 4, avl.inOrderTransversalData().size());
        System.out.println("Number of nodes: expected: 4 actual: " + avl.inOrderTransversalData().size());

        NodeData romeNode = avl.findNodeByCoordinates(new GeographicalLocation(44,10));
        assertNotNull("Rome should exist", romeNode);
        assertEquals("Node Rome should have 2 stations", 2, romeNode.getStations().size());
        System.out.println("Stations in node rome, expected: 2, actual: " + romeNode.getStations().size());

        System.out.println("<AVL>");
        System.out.println(PrintTree.toString(avl));

        System.out.println("------------------------------------");
    }

    @Test
    public void testRotationStations() {
        TimeZoneGroup tzGroup = TimeZoneGroup.CET;
        TimeZone tz = new TimeZone("Europe/Rome");
        Country country = new Country("IT");

        GeographicalLocation[] locations = {
                new GeographicalLocation(44, 10), // When first insert should be root
                new GeographicalLocation(45, 10), // Goes to the right
                new GeographicalLocation(46, 10), // Left rotation will happen, B will become root height will be 1
        };

        Station[] stations = {
                new Station(locations[0], tzGroup, tz, country, "A", 1, true, false, false),
                new Station(locations[1], tzGroup, tz, country, "B", 2, true, false, false),
                new Station(locations[2], tzGroup, tz, country, "C", 3, true, false, false),
        };

        AVL avl = new AVL();
        for (int i = 0; i < stations.length; i++) {
            GeographicalLocation loc = locations[i];
            Station sta = stations[i];
            NodeData node = avl.findNodeByCoordinates(loc);
            if (node != null) {
                node.addStation(sta);
            } else {
                avl.insert(new NodeData(loc, sta));
            }
        }

        List<NodeData> nodes = avl.inOrderTransversalData();
        assertEquals("should exist 3 nodes", 3, nodes.size());
        assertEquals("Name A", "A", nodes.get(0).getStations().get(0).getName());
        assertEquals("Name B", "B", nodes.get(1).getStations().get(0).getName());
        assertEquals("Name C", "C", nodes.get(2).getStations().get(0).getName());

        int h = avl.height();
        assertTrue("Avl Tree should be <= 1", h <= 1);

        System.out.println("<AVL testRotationStations>");
        System.out.println(PrintTree.toString(avl));
    }

    @Test
    public void testDoubleRotationStations() {
        TimeZoneGroup tzGroup = TimeZoneGroup.CET;
        TimeZone tz = new TimeZone("Europe/Rome");
        Country country = new Country("IT");

        GeographicalLocation[] locations = {
                new GeographicalLocation(46, 10), // When first insert should be root
                new GeographicalLocation(44, 10), // Goes to the left
                new GeographicalLocation(45, 10), // double rotation should happen(first left then right), C will become root height will be 1
        };

        Station[] stations = {
                new Station(locations[0], tzGroup, tz, country, "A", 1, true, false, false),
                new Station(locations[1], tzGroup, tz, country, "B", 2, true, false, false),
                new Station(locations[2], tzGroup, tz, country, "C", 3, true, false, false),
        };

        AVL avl = new AVL();
        for (int i = 0; i < stations.length; i++) {
            GeographicalLocation loc = locations[i];
            Station sta = stations[i];
            NodeData node = avl.findNodeByCoordinates(loc);
            if (node != null) {
                node.addStation(sta);
            } else {
                avl.insert(new NodeData(loc, sta));
            }
        }

        List<NodeData> nodes = avl.inOrderTransversalData();
        assertEquals("should exist 3 nodes", 3, nodes.size());
        assertEquals("Name B", "B", nodes.get(0).getStations().get(0).getName());
        assertEquals("Name C", "C", nodes.get(1).getStations().get(0).getName());
        assertEquals("Name A", "A", nodes.get(2).getStations().get(0).getName());

        int h = avl.height();
        assertTrue("Avl Tree should be <= 1", h <= 1);

        System.out.println("<AVL testDoubleRotationStations>");
        System.out.println(PrintTree.toString(avl));
    }


    // ===== Test with the 64k file ===== //
    @Test
    public void testImportFullStationAVL() {
        AVL avlTree = Parser64K.parseEUStations();
        System.out.println("============ Full Station AVL Tree ============ ");
        List<NodeData> nodes = avlTree.inOrderTransversalData();


        for (NodeData node : nodes) {
            assertNotNull("List of Stations null",node.getStations());
            assertNotNull("Node with null Cordinates", node.getCoordinate());
            assertTrue("Empty List of Stations",node.getStations().size() > 0);
            assertTrue("Invalid Coordinates",node.getCoordinate().getLatitude() >= -90 && node.getCoordinate().getLatitude() <= 90 &&
                    node.getCoordinate().getLongitude() >= -180 && node.getCoordinate().getLongitude() <= 180);
        }

        //==== Tree not properly ordered
        for (int i = 1; i < nodes.size(); i++) {
            assertTrue("AVL not sorted lexicographically at index " + i,
                    nodes.get(i-1).compareTo(nodes.get(i)) < 0);
        }

        //=== duplicate nodes with same coordinate
        Set<GeographicalLocation> coords = new HashSet<>();
        for (NodeData node : nodes) {
            assertTrue("Duplicate coordinate found: " + node.getCoordinate(),
                    coords.add(node.getCoordinate()));
        }

        //=== search not working properly
        NodeData middle = nodes.get(nodes.size() / 2);
        GeographicalLocation coordinate = middle.getCoordinate();

        NodeData nd = avlTree.findNodeByCoordinates(coordinate);
        assertEquals("Search returned wrong node", middle, nd);

        //=== impossible coordinate
        GeographicalLocation coordinate2 = new GeographicalLocation(999, 999);
        assertNull("Found non-existing coordinate", avlTree.findNodeByCoordinates(coordinate2));

        System.out.println(PrintTree.toString(avlTree));
    }


    // ===== Test with the stationTest1 file ===== //
    @Test
    public void testImportAVL7stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        System.out.println("============ Import 7 station AVL Tree ============ ");
        List<NodeData> nodes = avlTree.inOrderTransversalData();

        //==== invalid values====
        for (NodeData node : nodes) {
            assertNotNull("List of Stations null",node.getStations());
            assertNotNull("Node with null Cordinates", node.getCoordinate());
            assertTrue("Empty List of Stations",node.getStations().size() > 0);
            assertTrue("Invalid Coordinates",node.getCoordinate().getLatitude() >= -90 && node.getCoordinate().getLatitude() <= 90 &&
                        node.getCoordinate().getLongitude() >= -180 && node.getCoordinate().getLongitude() <= 180);
        }

        //==== Tree not properly ordered
        for (int i = 1; i < nodes.size(); i++) {
            assertTrue("AVL not sorted lexicographically at index " + i,
                    nodes.get(i-1).compareTo(nodes.get(i)) < 0);
        }

        //=== duplicate nodes with same coordinate
        Set<GeographicalLocation> coords = new HashSet<>();
        for (NodeData node : nodes) {
            assertTrue("Duplicate coordinate found: " + node.getCoordinate(),
                    coords.add(node.getCoordinate()));
        }

        //=== search not working properly
        NodeData middle = nodes.get(nodes.size() / 2);
        GeographicalLocation coordinate = middle.getCoordinate();

        NodeData nd = avlTree.findNodeByCoordinates(coordinate);
        assertEquals("Search returned wrong node", middle, nd);

        //=== impossible coordinate
        GeographicalLocation coordinate2 = new GeographicalLocation(999, 999);
        assertNull("Found non-existing coordinate", avlTree.findNodeByCoordinates(coordinate2));

        System.out.println(PrintTree.toString(avlTree));
    }


    // ===== Test with the stationTest2 file ===== //
    @Test
    public void testImportAVL4stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");
        System.out.println("============ 4 stations AVL Tree ============ ");

        List<NodeData> nodes = avlTree.inOrderTransversalData();
        for (NodeData node : nodes) {
            assertNotNull("List of Stations null",node.getStations());
            assertNotNull("Node with null Cordinates", node.getCoordinate());
            assertTrue("Empty List of Stations",node.getStations().size() > 0);
            assertTrue("Invalid Coordinates",node.getCoordinate().getLatitude() >= -90 && node.getCoordinate().getLatitude() <= 90 &&
                    node.getCoordinate().getLongitude() >= -180 && node.getCoordinate().getLongitude() <= 180);
        }

        //==== Tree not properly ordered
        for (int i = 1; i < nodes.size(); i++) {
            assertTrue("AVL not sorted lexicographically at index " + i,
                    nodes.get(i-1).compareTo(nodes.get(i)) < 0);
        }

        //=== duplicate nodes with same coordinate
        Set<GeographicalLocation> coords = new HashSet<>();
        for (NodeData node : nodes) {
            assertTrue("Duplicate coordinate found: " + node.getCoordinate(),
                    coords.add(node.getCoordinate()));
        }

        //=== search not working properly
        NodeData middle = nodes.get(nodes.size() / 2);
        GeographicalLocation coordinate = middle.getCoordinate();

        NodeData nd = avlTree.findNodeByCoordinates(coordinate);
        assertEquals("Search returned wrong node", middle, nd);

        //=== impossible coordinate
        GeographicalLocation coordinate2 = new GeographicalLocation(999, 999);
        assertNull("Found non-existing coordinate", avlTree.findNodeByCoordinates(coordinate2));

        System.out.println(PrintTree.toString(avlTree));
    }


    // ===== Test with the stationTest3 file ===== //
    @Test
    public void testImportAVL12stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        System.out.println("============ 12 stations AVL Tree ============ ");

        List<NodeData> nodes = avlTree.inOrderTransversalData();
        for (NodeData node : nodes) {
            assertNotNull("List of Stations null",node.getStations());
            assertNotNull("Node with null Cordinates", node.getCoordinate());
            assertTrue("Empty List of Stations",node.getStations().size() > 0);
            assertTrue("Invalid Coordinates",node.getCoordinate().getLatitude() >= -90 && node.getCoordinate().getLatitude() <= 90 &&
                    node.getCoordinate().getLongitude() >= -180 && node.getCoordinate().getLongitude() <= 180);

        }

        //==== Tree not properly ordered
        for (int i = 1; i < nodes.size(); i++) {
            assertTrue("AVL not sorted lexicographically at index " + i,
                    nodes.get(i-1).compareTo(nodes.get(i)) < 0);
        }

        //=== duplicate nodes with same coordinate
        Set<GeographicalLocation> coords = new HashSet<>();
        for (NodeData node : nodes) {
            assertTrue("Duplicate coordinate found: " + node.getCoordinate(),
                    coords.add(node.getCoordinate()));
        }

        //=== search not working properly
        NodeData middle = nodes.get(nodes.size() / 2);
        GeographicalLocation coordinate = middle.getCoordinate();

        NodeData nd = avlTree.findNodeByCoordinates(coordinate);
        assertEquals("Search returned wrong node", middle, nd);

        //=== impossible coordinate
        GeographicalLocation coordinate2 = new GeographicalLocation(999, 999);
        assertNull("Found non-existing coordinate", avlTree.findNodeByCoordinates(coordinate2));

        System.out.println(PrintTree.toString(avlTree));
    }


    // ===== Test with the Trash File file ===== //
    @Test
    public void testImportAVLTrashFile() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTrashTest.csv");
        System.out.println("============ 12 stations AVL Tree ============ ");

        List<NodeData> nodes = avlTree.inOrderTransversalData();
        int validNodesCount = nodes.size();

        // 1. Check if the number of nodes is equal to the expected (only 5 out of 12 are correct)
        int expectedValid = 5;
        assertEquals("Tree should contain only valid stations", expectedValid, validNodesCount);

        for (NodeData node : nodes) {
            for (Station s : node.getStations()) {
                assertNotNull("Station name should not be null or empty", s.getName());
                assertFalse("Station name should not be empty", s.getName().isEmpty());
                assertNotNull("Country should not be null", s.getCountry());
                assertNotNull("Location should not be null", s.getLocation());
                assertFalse("Latitude should be valid", Double.isNaN(s.getLocation().getLatitude()));
                assertFalse("Longitude should be valid", Double.isNaN(s.getLocation().getLongitude()));
            }
        }

        //==== Tree not properly ordered
        for (int i = 1; i < nodes.size(); i++) {
            assertTrue("AVL not sorted lexicographically at index " + i,
                    nodes.get(i-1).compareTo(nodes.get(i)) < 0);
        }

        //=== duplicate nodes with same coordinate
        Set<GeographicalLocation> coords = new HashSet<>();
        for (NodeData node : nodes) {
            assertTrue("Duplicate coordinate found: " + node.getCoordinate(),
                    coords.add(node.getCoordinate()));
        }

        //=== search not working properly
        NodeData middle = nodes.get(nodes.size() / 2);
        GeographicalLocation coordinate = middle.getCoordinate();

        NodeData nd = avlTree.findNodeByCoordinates(coordinate);
        assertEquals("Search returned wrong node", middle, nd);

        //=== impossible coordinate
        GeographicalLocation coordinate2 = new GeographicalLocation(999, 999);
        assertNull("Found non-existing coordinate", avlTree.findNodeByCoordinates(coordinate2));

        System.out.println(PrintTree.toString(avlTree));
    }


}
