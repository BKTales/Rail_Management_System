package org.dei;

import java.util.List;

import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.NodeData;

import org.junit.Test;
import static org.junit.Assert.*;
import org.dei.Sprint2.Parser64K;

public class AVLQueriesTest {

        @Test
        public void testSearchNodesLessThan_7stations() {
            AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");

            List<NodeData> result = avlTree.searchByCoordinatesLessThan(45.0, 14.0);
            assertFalse(result.isEmpty());
            for (NodeData nd : result) {
                GeographicalLocation loc = nd.getCoordinate();
                assertTrue(loc.getLatitude() < 45.0);
                assertTrue(loc.getLongitude() < 13.0);
            }
        }

        @Test
        public void testSearchNodesLessThan_fullFile() {
            AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/train_stations_europe.csv");

            List<NodeData> result = avlTree.searchByCoordinatesLessThan(56.74168,12.94612);


            assertFalse(result.isEmpty());
            for (NodeData nd : result) {
                GeographicalLocation loc = nd.getCoordinate();
                assertTrue(loc.getLatitude() <56.74168);
                assertTrue( loc.getLongitude() <12.94612);
            }


        }


        @Test
        public void testSearchNodesExact_fullFile() {
            AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/train_stations_europe.csv");
            // 41.1579, -8.6291 (Porto Campanhã)
            List<NodeData> result = avlTree.searchByCoordinatesExact(41.148823,-8.584932);

            assertTrue(result.isEmpty());
            for (NodeData nd : result) {
                GeographicalLocation loc = nd.getCoordinate();
                assertEquals(41.148823, loc.getLatitude(), 0.0001);
                assertEquals(-8.584932, loc.getLongitude(), 0.0001);
            }
        }

}
