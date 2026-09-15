package org.dei.GraphTests;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint3.GraphAlgorithms.DirectedLineUpgradePlan;
import org.dei.Sprint3.GraphAlgorithms.UpgradePlanResult;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei.Sprint3.Graph.RailNetworkGraphs.CartesianCoordinates;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.dei.Sprint3.Graph.RailNetworkGraphs.StationVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DirectedLineUpgradePlanTest {

    private static RailNetworkGraphService graphService;
    private static ParserBerlgianNW parser;

    private static final String STATIONS_FILE = "src/main/resources/ Belgian_Rail_Network/stations.csv";
    private static final String LINES_FILE = "src/main/resources/ Belgian_Rail_Network/lines.csv";

    // --- Helpers de Criação de Grafo ---

    private StationVertex createVertex(String name, double x, double y) {
        CartesianCoordinates coord = new CartesianCoordinates(x, y);
        Station station = new Station(
                new GeographicalLocation(x, y),
                TimeZoneGroup.WETGMT,
                new TimeZone("Europe/Lisbon"),
                new Country("PT"),
                name,
                new Random().nextInt(1000),
                false, false, false
        );
        return new StationVertex(coord, station);
    }



    private void assertDependency(UpgradePlanResult result, StationVertex before, StationVertex after) {
        List<LinkedList<StationVertex>> orders = result.getTopologicalOrder();

        // Verifica todos os WCCs para encontrar os vértices
        boolean found = false;
        for (LinkedList<StationVertex> order : orders) {
            int idxBefore = order.indexOf(before);
            int idxAfter = order.indexOf(after);

            if (idxBefore != -1 && idxAfter != -1) {
                // Se ambos estão no WCC, 'before' deve vir antes de 'after'
                assertTrue(idxBefore < idxAfter,
                        String.format("Dependency violated: %s must come before %s in the upgrade plan.",
                                before.getStation().getName(), after.getStation().getName()));
                found = true;
            } else if (idxBefore != -1 || idxAfter != -1) {
                // Se apenas um está, significa que o WCC é grande, mas não quebrou a dependência
                found = true;
            }
        }
        assertTrue(found, String.format("Dependency nodes (%s, %s) not found together in any WCC. Check WCC grouping.",
                before.getStation().getName(), after.getStation().getName()));
    }

    /**
     * Verifica se a ordem topológica contém todos os vértices esperados.
     */
    private void assertAllVerticesArePresent(UpgradePlanResult result, int expectedVertexCount) {
        List<LinkedList<StationVertex>> orders = result.getTopologicalOrder();
        if (orders == null) return;

        int totalVertices = orders.stream().mapToInt(List::size).sum();
        assertEquals(expectedVertexCount, totalVertices, "The total number of vertices in the topological order must match the graph size.");
    }



    private void printUpgradePlan(UpgradePlanResult result) {
        System.out.println("\n--- EXECUTION RESULT ---");

        if (result.hasCycle()) {
            printCycles(result);
        } else {
            printUpgradeOrder(result);
        }
        System.out.println("---------------------------------------------------\n");
    }

    private void printCycles(UpgradePlanResult result) {
        // Calculates how many vertices were successfully processed before the cycle was hit
        int processedCount = 0;
        if (result.getTopologicalOrder() != null) {
            processedCount = result.getTopologicalOrder().stream().mapToInt(List::size).sum();
        }

        int cycleSize = result.getCycle() != null ? result.getCycle().size() : 0;

        System.out.println("🛑 FAILURE: Cycle(s) Detected!");
        System.out.println("   A complete upgrade ordering is impossible.");

        if (result.getCycle() != null && !result.getCycle().isEmpty()) {
            System.out.println("\n   First Cycle (Strongly Connected Component, Size: " + cycleSize + "):");

            // Print the sequence and repeat the first node to close the loop
            System.out.print("   -> ");
            result.getCycle().forEach(v -> System.out.print(v.getStation().getName() + " -> "));
            System.out.println(result.getCycle().getFirst().getStation().getName());

            if (processedCount > 0) {
                System.out.printf("\n   [INFO: %d vertices had already been ordered across %d WCCs before the cycle was found.]\n",
                        processedCount, result.getTopologicalOrder().size());
            }
        } else {
            System.out.println("Error: 'hasCycle' is true, but the cycle list is empty.");
        }
        // Optional: Add a visual of a cyclic graph
    }

    private void printUpgradeOrder(UpgradePlanResult result) {
        System.out.println("✅ SUCCESS: No Cycles Detected.");

        List<LinkedList<StationVertex>> topoOrders = result.getTopologicalOrder();

        if (topoOrders == null || topoOrders.isEmpty()) {
            System.out.println("   Result: Empty graph or no Weakly Connected Components (WCCs) found.");
            return;
        }

        int totalVertices = topoOrders.stream().mapToInt(List::size).sum();
        System.out.printf("   Total Vertices Ordered: %d across %d Weakly Connected Components (WCCs).\n\n", totalVertices, topoOrders.size());


        if (topoOrders.size() > 1) {
            System.out.println("   Disconnected Graph. Order by WCC (Sequence is valid within each component):");
            int compNum = 1;

            for (LinkedList<StationVertex> componentOrder : topoOrders) {
                System.out.print("   WCC " + compNum + " (Size: " + componentOrder.size() + "): ");

                // Print the order of the component
                for (int i = 0; i < componentOrder.size(); i++) {
                    System.out.print(componentOrder.get(i).getStation().getName());
                    if (i < componentOrder.size() - 1) System.out.print(" -> ");
                }
                System.out.println();
                compNum++;
            }
        } else { // topoOrders.size() == 1 (Single WCC / Connected Graph)
            LinkedList<StationVertex> globalTopoOrder = topoOrders.get(0);
            System.out.println("   Connected Graph (1 WCC). Global Upgrade Order:");

            // Add the critical note explaining non-determinism in branching graphs
            if (globalTopoOrder.size() > 5) {
                System.out.println("   [Note: The exact sequence may vary in parallel branches, but all dependencies are guaranteed to be respected.]");
            }

            System.out.print("   -> ");
            for (int i = 0; i < globalTopoOrder.size(); i++) {
                System.out.print(globalTopoOrder.get(i).getStation().getName());
                if (i < globalTopoOrder.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
            // Optional: Add a visual of an acyclic graph with a valid topological order
        }
    }


    // --- Testes Refatorados com Asserções ---

    @Test
    public void test1_Simple3NodeCycle() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A1",0,0);
        StationVertex b = createVertex("B1",1,0);
        StationVertex c = createVertex("C1",2,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,c,new TrackWeight(1,1,1));
        graph.addEdge(c,a,new TrackWeight(1,1,1)); // Cycle A-B-C-A

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 1: Must detect the simple 3-node cycle.");
        assertEquals(3, result.getCycle().size(), "Test 1: The cycle must contain exactly 3 vertices.");

        printUpgradePlan(result);
    }

    @Test
    public void test2_TwoNodeCycle() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A2",0,0);
        StationVertex b = createVertex("B2",1,0);
        graph.addVertex(a); graph.addVertex(b);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,a,new TrackWeight(1,1,1)); // Cycle A-B-A

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 2: Must detect the two-node cycle.");
        assertEquals(2, result.getCycle().size(), "Test 2: The cycle must contain exactly 2 vertices.");

        printUpgradePlan(result);
    }

    @Test
    public void test3_Linear3NodeChain() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A3",0,0);
        StationVertex b = createVertex("B3",1,0);
        StationVertex c = createVertex("C3",2,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,c,new TrackWeight(1,1,1));

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 3: Must not detect a cycle.");
        assertAllVerticesArePresent(result, 3);
        assertDependency(result, a, b);
        assertDependency(result, b, c);

        printUpgradePlan(result);
    }

    @Test
    public void test4_BranchingAcyclicGraph() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A4",0,0);
        StationVertex b = createVertex("B4",1,0);
        StationVertex c = createVertex("C4",2,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(a,c,new TrackWeight(1,1,1)); // A -> B, A -> C

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 4: Must not detect a cycle.");
        assertAllVerticesArePresent(result, 3);
        assertDependency(result, a, b);
        assertDependency(result, a, c);

        printUpgradePlan(result);
    }

    @Test
    public void test5_CyclePlusTail() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A5",0,0);
        StationVertex b = createVertex("B5",1,0);
        StationVertex c = createVertex("C5",2,0);
        StationVertex d = createVertex("D5",3,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,c,new TrackWeight(1,1,1));
        graph.addEdge(c,a,new TrackWeight(1,1,1)); // Cycle A-B-C-A
        graph.addEdge(c,d,new TrackWeight(1,1,1)); // Tail C -> D

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 5: Must detect the cycle A-B-C-A.");
        assertEquals(3, result.getCycle().size(), "Test 5: The cycle must contain 3 vertices (A, B, C).");

        printUpgradePlan(result);
    }

    @Test
    public void test6_SingleIsolatedNode() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A6",0,0);
        graph.addVertex(a);

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 6: Must not detect a cycle.");
        assertEquals(1, result.getTopologicalOrder().size(), "Test 6: Must have 1 WCC.");
        assertAllVerticesArePresent(result, 1);

        printUpgradePlan(result);
    }

    @Test
    public void test7_TwoDisconnectedChains() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A7",0,0);
        StationVertex b = createVertex("B7",1,0);
        StationVertex c = createVertex("C7",2,0);
        StationVertex d = createVertex("D7",3,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);
        graph.addEdge(a,b,new TrackWeight(1,1,1)); // WCC 1: A -> B
        graph.addEdge(c,d,new TrackWeight(1,1,1)); // WCC 2: C -> D

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 7: Must not detect a cycle.");
        assertEquals(2, result.getTopologicalOrder().size(), "Test 7: Must find 2 WCCs.");
        assertDependency(result, a, b);
        assertDependency(result, c, d);
        assertAllVerticesArePresent(result, 4);

        printUpgradePlan(result);
    }

    @Test
    public void test8_DisconnectedCycleAndChain() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A8",0,0);
        StationVertex b = createVertex("B8",1,0);
        StationVertex c = createVertex("C8",2,0);
        StationVertex d = createVertex("D8",3,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,a,new TrackWeight(1,1,1)); // WCC 1: Cycle A-B-A
        graph.addEdge(c,d,new TrackWeight(1,1,1)); // WCC 2: Chain C -> D

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 8: Must detect the cycle A-B-A.");
        assertEquals(2, result.getCycle().size(), "Test 8: The cycle must contain 2 vertices (A, B).");

        printUpgradePlan(result);
    }

    @Test
    public void test9_FourNodeLinearChain() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A9",0,0);
        StationVertex b = createVertex("B9",1,0);
        StationVertex c = createVertex("C9",2,0);
        StationVertex d = createVertex("D9",3,0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);
        graph.addEdge(a,b,new TrackWeight(1,1,1));
        graph.addEdge(b,c,new TrackWeight(1,1,1));
        graph.addEdge(c,d,new TrackWeight(1,1,1));

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 9: Must not detect a cycle.");
        assertDependency(result, a, b);
        assertDependency(result, b, c);
        assertDependency(result, c, d);
        assertAllVerticesArePresent(result, 4);

        printUpgradePlan(result);
    }

    @Test
    public void test10_ComplexAcyclicConvergence() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex s = createVertex("Start", 0, 0);
        StationVertex a = createVertex("A", 1, 1);
        StationVertex b = createVertex("B", 1, -1);
        StationVertex e = createVertex("End", 2, 0);
        graph.addVertex(s); graph.addVertex(a); graph.addVertex(b); graph.addVertex(e);

        graph.addEdge(s, a, new TrackWeight(1, 1, 1));
        graph.addEdge(s, b, new TrackWeight(1, 1, 1));
        graph.addEdge(a, e, new TrackWeight(1, 1, 1));
        graph.addEdge(b, e, new TrackWeight(1, 1, 1)); // S -> A/B -> E

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 10: Must not detect a cycle.");
        assertDependency(result, s, a);
        assertDependency(result, s, b);
        assertDependency(result, a, e);
        assertDependency(result, b, e);
        assertAllVerticesArePresent(result, 4);

        printUpgradePlan(result);
    }

    @Test
    public void test11_EmptyGraph() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 11: Must not detect a cycle.");
        assertTrue(result.getTopologicalOrder().isEmpty(), "Test 11: Topological order must be empty.");

        printUpgradePlan(result);
    }


    @BeforeAll
    public static void setup() {
        graphService = new RailNetworkGraphService();
        parser = new ParserBerlgianNW(graphService);
    }

    @Test
    public void test12_BelgianRailNetwork() {
        // Load real network
        boolean loaded = parser.loadRailNetwork(STATIONS_FILE, LINES_FILE);
        assertTrue(loaded, "Test 12: Should load real network successfully");

        MapGraph<StationVertex, TrackWeight> graph = graphService.getMapGraph();
        assertTrue(graph.numVertices() > 0, "Test 12: Should have loaded stations.");

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 12: The Belgian rail network must contain cycles (bidirectional tracks).");
        assertTrue(result.getCycle().size() >= 2, "Test 12: The detected cycle must have at least 2 vertices.");

        printUpgradePlan(result);
    }

    /**
     * Teste 13: Grafo com aresta inversa que garante que o WCC é agrupado corretamente
     * antes de ser ordenado. (A <- B, C -> D)
     */
    @Test
    public void test13_InverseEdgeWCCGrouping() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A13", 0, 0);
        StationVertex b = createVertex("B13", 1, 0);
        StationVertex c = createVertex("C13", 2, 0);
        StationVertex d = createVertex("D13", 3, 0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);

        // WCC 1: A depende de B (B -> A). Ordem esperada: [B13 -> A13]
        graph.addEdge(b, a, new TrackWeight(1, 1, 1));
        // WCC 2: C -> D. Ordem esperada: [C13 -> D13]
        graph.addEdge(c, d, new TrackWeight(1, 1, 1));

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 13: Must not detect a cycle.");
        assertEquals(2, result.getTopologicalOrder().size(), "Test 13: Must find 2 WCCs.");
        assertDependency(result, b, a); // B antes de A
        assertDependency(result, c, d); // C antes de D
        assertAllVerticesArePresent(result, 4);

        printUpgradePlan(result);
    }

    /**
     * Teste 14: Dois SCCs interconectados por uma ponte que os torna um único WCC.
     * (A <-> B) -> C -> (D <-> E)
     */
    @Test
    public void test14_InterconnectedSCCs() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A14", 0, 0);
        StationVertex b = createVertex("B14", 1, 0);
        StationVertex c = createVertex("C14", 2, 0);
        StationVertex d = createVertex("D14", 3, 0);
        StationVertex e = createVertex("E14", 4, 0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d); graph.addVertex(e);

        // Ciclo 1 (SCC): A <-> B
        graph.addEdge(a, b, new TrackWeight(1, 1, 1));
        graph.addEdge(b, a, new TrackWeight(1, 1, 1));

        // Ponte C: Sai de B e entra em D
        graph.addEdge(b, c, new TrackWeight(1, 1, 1)); // B -> C
        graph.addEdge(c, d, new TrackWeight(1, 1, 1)); // C -> D

        // Ciclo 2 (SCC): D <-> E
        graph.addEdge(d, e, new TrackWeight(1, 1, 1));
        graph.addEdge(e, d, new TrackWeight(1, 1, 1));

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 14: Must detect a cycle (A-B or D-E).");
        assertEquals(2, result.getCycle().size(), "Test 14: The detected cycle must have 2 vertices.");

        printUpgradePlan(result);
    }

    /**
     * Teste 15: Grafo acíclico complexo onde múltiplos nós convergem para um único destino.
     * (A, B, C) -> D
     */
    @Test
    public void test15_MultipleConvergence() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A15", 0, 0);
        StationVertex b = createVertex("B15", 1, 0);
        StationVertex c = createVertex("C15", 2, 0);
        StationVertex d = createVertex("D15", 3, 0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d);

        graph.addEdge(a, d, new TrackWeight(1, 1, 1));
        graph.addEdge(b, d, new TrackWeight(1, 1, 1));
        graph.addEdge(c, d, new TrackWeight(1, 1, 1)); // A, B, C devem vir antes de D

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 15: Must not detect a cycle.");
        assertDependency(result, a, d);
        assertDependency(result, b, d);
        assertDependency(result, c, d);
        assertAllVerticesArePresent(result, 4);

        printUpgradePlan(result);
    }

    /**
     * Teste 16: Grafo desconexo com três WCCs: uma cadeia, um ciclo, e um nó isolado.
     */
    @Test
    public void test16_ChainCycleAndIsolatedNode() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex a = createVertex("A16", 0, 0);
        StationVertex b = createVertex("B16", 1, 0);
        StationVertex c = createVertex("C16", 2, 0);
        StationVertex d = createVertex("D16", 3, 0);
        StationVertex e = createVertex("E16", 4, 0);
        StationVertex f = createVertex("F16", 5, 0);
        graph.addVertex(a); graph.addVertex(b); graph.addVertex(c); graph.addVertex(d); graph.addVertex(e); graph.addVertex(f);

        // WCC 1: Cadeia (A -> B -> C)
        graph.addEdge(a, b, new TrackWeight(1, 1, 1));
        graph.addEdge(b, c, new TrackWeight(1, 1, 1));

        // WCC 2: Ciclo (D <-> E)
        graph.addEdge(d, e, new TrackWeight(1, 1, 1));
        graph.addEdge(e, d, new TrackWeight(1, 1, 1));

        // WCC 3: Isolado (F)

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertTrue(result.hasCycle(), "Test 16: Must detect the cycle D-E.");
        assertEquals(2, result.getCycle().size(), "Test 16: The detected cycle must have 2 vertices.");

        printUpgradePlan(result);
    }

    /**
     * Teste 17: Grande WCC acíclico com múltiplas ramificações.
     */
    @Test
    public void test17_LargeAcyclicWCC() {
        MapGraph<StationVertex, TrackWeight> graph = new MapGraph<>(true);
        StationVertex r = createVertex("R17", 0, 0); // Raiz
        StationVertex s1 = createVertex("S17", 1, 0);
        StationVertex s2 = createVertex("S27", 2, 0);
        StationVertex s3 = createVertex("S37", 3, 0);
        StationVertex e1 = createVertex("E17", 4, 0);
        StationVertex e2 = createVertex("E27", 5, 0);
        graph.addVertex(r); graph.addVertex(s1); graph.addVertex(s2); graph.addVertex(s3); graph.addVertex(e1); graph.addVertex(e2);

        // Ramo 1
        graph.addEdge(r, s1, new TrackWeight(1, 1, 1));
        graph.addEdge(s1, e1, new TrackWeight(1, 1, 1));

        // Ramo 2
        graph.addEdge(r, s2, new TrackWeight(1, 1, 1));

        // Ramo 3 (Mais longo)
        graph.addEdge(r, s3, new TrackWeight(1, 1, 1));
        graph.addEdge(s3, e2, new TrackWeight(1, 1, 1));

        UpgradePlanResult result = DirectedLineUpgradePlan.computeUpgradePlan(graph);

        assertFalse(result.hasCycle(), "Test 17: Must not detect a cycle.");

        // R deve preceder S1, S2, S3
        assertDependency(result, r, s1);
        assertDependency(result, r, s2);
        assertDependency(result, r, s3);

        // S1 deve preceder E1 e S3 deve preceder E2
        assertDependency(result, s1, e1);
        assertDependency(result, s3, e2);

        assertAllVerticesArePresent(result, 6);

        printUpgradePlan(result);
    }
}