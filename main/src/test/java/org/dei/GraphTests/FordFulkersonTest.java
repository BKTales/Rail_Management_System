package org.dei.GraphTests;

import org.dei.Sprint3.GraphAlgorithms.FordFulkerson;
import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class FordFulkersonTest {

    /**
     * Helper to create dummy stations for synthetic graph tests.
     */
    private Station createDummyStation(int id, String name) {
        return new Station(
                new GeographicalLocation(0, 0),
                TimeZoneGroup.CET,
                new TimeZone("Europe/Brussels"),
                new Country("BE"),
                name,
                id,
                false, false, false
        );
    }

    @Nested
    @DisplayName("Unit Tests (Controlled Synthetic Graph)")
    class SyntheticGraphTests {

        private RailNetworkGraphService graphService;

        @BeforeEach
        void setUp() {
            graphService = new RailNetworkGraphService();
        }

        @Test
        @DisplayName("Simple Linear Flow: Source -> A -> Sink")
        void testSimplePath() {
            // Setup stations
            graphService.addStation(createDummyStation(1, "Source"), 0, 0);
            graphService.addStation(createDummyStation(2, "A"), 10, 0);
            graphService.addStation(createDummyStation(3, "Sink"), 20, 0);

            // Setup Lines: S -> A (Cap 100), A -> Sink (Cap 50)
            // The bottleneck is 50, so max flow should be 50.
            graphService.addLine(1, 2, 10.0, 100.0, 0);
            graphService.addLine(2, 3, 10.0, 50.0, 0);

            StationVertex source = graphService.findStationById(1);
            StationVertex sink = graphService.findStationById(3);

            // Execute
            FordFulkerson alg = new FordFulkerson(graphService.getMapGraph(), source, sink);

            // Assert
            assertEquals(50.0, alg.getMaxFlow(), 0.001,
                    "Max flow should be limited by the smallest capacity edge (Bottleneck = 50)");
        }

        @Test
        @DisplayName("Split Flow: Multiple Paths Summation")
        void testSplitFlow() {
            /* * Scenario:
             * /-- (50) --> A -- (50) --\
             * S                          T
             * \-- (50) --> B -- (50) --/
             *
             * Expected Total Flow: 50 + 50 = 100
             */
            graphService.addStation(createDummyStation(1, "S"), 0, 0);
            graphService.addStation(createDummyStation(2, "A"), 0, 0);
            graphService.addStation(createDummyStation(3, "B"), 0, 0);
            graphService.addStation(createDummyStation(4, "T"), 0, 0);

            graphService.addLine(1, 2, 10, 50, 0); // S->A
            graphService.addLine(2, 4, 10, 50, 0); // A->T
            graphService.addLine(1, 3, 10, 50, 0); // S->B
            graphService.addLine(3, 4, 10, 50, 0); // B->T

            StationVertex source = graphService.findStationById(1);
            StationVertex sink = graphService.findStationById(4);

            FordFulkerson alg = new FordFulkerson(graphService.getMapGraph(), source, sink);

            assertEquals(100.0, alg.getMaxFlow(), 0.001, "Flow should be the sum of both independent branches");
        }

        @Test
        @DisplayName("Complex Case: Backtracking via Residual Graph")
        void testComplexFlow() {
            /*
             * Classic Ford-Fulkerson Counter-example for Greedy DFS:
             * S -> A: 1000
             * S -> B: 1000
             * A -> B: 1    (Small cross edge)
             * A -> T: 1000
             * B -> T: 1000
             * * If DFS greedily takes S->A->B->T (flow 1), it might block future paths
             * unless "backward edges" are used to undo the flow on A->B.
             * Expected Max Flow: 2000
             */
            graphService.addStation(createDummyStation(1, "S"), 0, 0);
            graphService.addStation(createDummyStation(2, "A"), 0, 0);
            graphService.addStation(createDummyStation(3, "B"), 0, 0);
            graphService.addStation(createDummyStation(4, "T"), 0, 0);

            graphService.addLine(1, 2, 10, 1000, 0);
            graphService.addLine(1, 3, 10, 1000, 0);
            graphService.addLine(2, 3, 5,  1,    0);
            graphService.addLine(2, 4, 10, 1000, 0);
            graphService.addLine(3, 4, 10, 1000, 0);

            StationVertex source = graphService.findStationById(1);
            StationVertex sink = graphService.findStationById(4);

            FordFulkerson alg = new FordFulkerson(graphService.getMapGraph(), source, sink);

            assertEquals(2000.0, alg.getMaxFlow(), 0.001,
                    "Algorithm must utilize backward edges to correct greedy DFS choices");
        }
    }

    @Nested
    class RealNetworkTests {

        private RailNetworkGraphService graphService;
        private ParserBerlgianNW parser;

        private static final String stationsTestFile = "src/main/resources/ Belgian_Rail_Network/stations.csv";
        private static final String linesTestFile = "src/main/resources/ Belgian_Rail_Network/lines.csv";

        @BeforeEach
        void setUpRealNetwork() {
            graphService = new RailNetworkGraphService();
            parser = new ParserBerlgianNW(graphService);

            boolean loaded = parser.loadRailNetwork(stationsTestFile, linesTestFile);
            assertTrue(loaded, "Failed to load CSV files. Please check file paths.");
            assertTrue(graphService.getStationCount() > 0, "Graph should contain stations after loading.");
        }

        @Test
        @DisplayName("End-to-End Flow Calculation between arbitrary stations")
        void testRealNetworkFlow() {
            // Dynamically select two distant stations to ensure the test is robust
            Iterator<StationVertex> it = graphService.getMapGraph().vertices().iterator();

            if (!it.hasNext()) fail("Graph is empty, cannot run test.");

            StationVertex source = it.next();
            StationVertex sink = null;

            // Skip a few stations to pick a distinct sink
            int steps = 0;
            while(it.hasNext()) {
                sink = it.next();
                steps++;
                if(steps > 5) break;
            }

            assertNotNull(source, "Source station is null");
            assertNotNull(sink, "Sink station is null");
            assertNotEquals(source, sink, "Source and Sink must be different stations");

            System.out.println("Running Max Flow on Real Network...");
            System.out.println("Source: " + source.getStation().getName());
            System.out.println("Sink:   " + sink.getStation().getName());

            // Measure execution time
            long startTime = System.nanoTime();
            FordFulkerson alg = new FordFulkerson(graphService.getMapGraph(), source, sink);
            long endTime = System.nanoTime();

            double durationMs = (endTime - startTime) / 1e6;
            System.out.printf("Max Flow: %.0f\n", alg.getMaxFlow());
            System.out.printf("Execution Time: %.2f ms\n", durationMs);

            assertTrue(alg.getMaxFlow() >= 0, "Max flow result cannot be negative");
            assertEquals(source, alg.getSource(), "Source getter should return correct vertex");
        }
    }
}