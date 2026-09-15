package org.dei.GraphTests.MSTRelatedTests;

import org.dei.Sprint3.GraphAlgorithms.MST;
import org.dei.Sprint3.Graph.Graph;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeight;
import org.dei.Sprint3.Graph.map.MapGraph;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Minimum Spanning Tree (MST) Tests")
public class MSTTests {

    private MapGraph<String, TrackWeight> simpleGraph;

    @BeforeEach
    void setUp() {
        // Create a simple directed graph for testing
        // Vertices: A, B, C, D
        simpleGraph = new MapGraph<>(true); // directed

        simpleGraph.addVertex("A");
        simpleGraph.addVertex("B");
        simpleGraph.addVertex("C");
        simpleGraph.addVertex("D");

        // Add edges with distances
        // A <-> B (10km)
        simpleGraph.addEdge("A", "B", new TrackWeight(0, 10, 0));
        simpleGraph.addEdge("B", "A", new TrackWeight(0, 10, 0));

        // B <-> C (5km)
        simpleGraph.addEdge("B", "C", new TrackWeight(0, 5, 0));
        simpleGraph.addEdge("C", "B", new TrackWeight(0, 5, 0));

        // A <-> C (20km) - This should be skipped in MST because A-B-C is 15km
        simpleGraph.addEdge("A", "C", new TrackWeight(0, 20, 0));
        simpleGraph.addEdge("C", "A", new TrackWeight(0, 20, 0));

        // C <-> D (2km)
        simpleGraph.addEdge("C", "D", new TrackWeight(0, 2, 0));
        simpleGraph.addEdge("D", "C", new TrackWeight(0, 2, 0));
    }

    @Test
    @DisplayName("Convert directed to undirected graph")
    void testConvertToUndirected() {
        System.out.println("\n=== Test: Convert directed to undirected graph ===");
        System.out.println("Input: Directed graph with " + simpleGraph.numVertices() + " vertices and " + simpleGraph.numEdges() + " edges");
        
        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);

        assertFalse(undirected.isDirected(), "Graph should be undirected");
        System.out.println("✓ Graph is undirected: " + !undirected.isDirected());
        
        assertEquals(4, undirected.numVertices(), "Should have 4 vertices");
        System.out.println("✓ Number of vertices: " + undirected.numVertices() + " (expected: 4)");

        // In undirected graph, A-B and B-A count as the two edge
        // Total unique edges: A-B, B-C, A-C, C-D = 4 edges
        // + the opposite (4) = 8
        assertEquals(8, undirected.numEdges(), "Should have 4 unique undirected edges");
        System.out.println("✓ Number of edges: " + undirected.numEdges() + " (expected: 8)");
    }

    @Test
    @DisplayName("Kruskal MST - Correct edge count")
    void testKruskalEdgeCount() {
        System.out.println("\n=== Test: Kruskal MST - Correct edge count ===");
        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);
        System.out.println("Created undirected graph with " + undirected.numVertices() + " vertices and " + undirected.numEdges() + " edges");
        
        Graph<String, TrackWeight> mst = MST.kruskal(undirected);
        System.out.println("MST computed with " + mst.numVertices() + " vertices and " + mst.numEdges() + " edges");

        assertEquals(4, mst.numVertices(), "MST should have all vertices");
        System.out.println("✓ MST has all vertices: " + mst.numVertices() + " (expected: 4)");

        // MST property: Edges = (Vertices - 1 (for connected graph)) * 2(both sides)
        assertEquals(6, mst.numEdges(), "MST should have V-1 edges (3 edges)");
        System.out.println("✓ MST has correct number of edges: " + mst.numEdges() + " (expected: 6, which is (V-1)*2)");
    }

    @Test
    @DisplayName("Kruskal MST - Validate total weight")
    void testKruskalTotalWeight() {
        System.out.println("\n=== Test: Kruskal MST - Validate total weight ===");
        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);
        Graph<String, TrackWeight> mst = MST.kruskal(undirected);

        double totalDistance = 0;
        System.out.println("Calculating total distance of MST edges:");
        for (var edge : mst.edges()) {
            double edgeDistance = edge.getWeight().getDistance();
            totalDistance += edgeDistance;
            System.out.println("  Edge " + edge.getVOrig() + " <-> " + edge.getVDest() + ": " + edgeDistance + " km");
        }

        // Expected MST path: (D-C (2) + C-B (5) + B-A (10)) * 2 = 34 km
        // The edge A-C (20) is skipped because 20 > 10+5
        System.out.println("Total MST distance: " + totalDistance + " km (expected: 34.0 km)");
        System.out.println("Note: Edge A-C (20 km) should be skipped because path A-B-C (15 km) is shorter");
        assertEquals(34.0, totalDistance, 0.001, "Total MST distance should be 34.0");
        System.out.println("✓ Total distance is correct");
    }

    @Test
    @DisplayName("Kruskal MST - Check specific edges existence")
    void testKruskalSpecificEdges() {
        System.out.println("\n=== Test: Kruskal MST - Check specific edges existence ===");
        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);
        Graph<String, TrackWeight> mst = MST.kruskal(undirected);

        // Edges that should exist
        System.out.println("Checking edges that should exist in MST:");
        assertNotNull(mst.edge("C", "D"), "Edge C-D should exist (shortest: 2)");
        System.out.println("  ✓ Edge C-D exists (shortest: 2 km)");
        
        assertNotNull(mst.edge("B", "C"), "Edge B-C should exist (medium: 5)");
        System.out.println("  ✓ Edge B-C exists (medium: 5 km)");
        
        assertNotNull(mst.edge("A", "B"), "Edge A-B should exist (necessary: 10)");
        System.out.println("  ✓ Edge A-B exists (necessary: 10 km)");

        // Edges that should not exist (created cycle or too expensive)
        System.out.println("Checking edges that should NOT exist in MST:");
        assertNull(mst.edge("A", "C"), "Edge A-C should not exist (too big: 20)");
        System.out.println("  ✓ Edge A-C does not exist (too expensive: 20 km > 15 km via A-B-C)");
    }

    @Test
    @DisplayName("Kruskal on disconnected graph")
    void testKruskalDisconnected() {
        System.out.println("\n=== Test: Kruskal on disconnected graph ===");
        System.out.println("Adding isolated vertex 'E' to create disconnected graph");
        simpleGraph.addVertex("E");
        System.out.println("Graph now has " + simpleGraph.numVertices() + " vertices");

        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);
        Graph<String, TrackWeight> mst = MST.kruskal(undirected);
        System.out.println("MST computed with " + mst.numVertices() + " vertices and " + mst.numEdges() + " edges");

        assertEquals(5, mst.numVertices(), "MST should have 5 vertices");
        System.out.println("✓ MST has all vertices: " + mst.numVertices() + " (expected: 5)");
        
        assertEquals(6, mst.numEdges(), "MST should still have 6 edges (E is isolated)");
        System.out.println("✓ MST has correct edges: " + mst.numEdges() + " (expected: 6, vertex E is isolated)");
    }

    @Test
    @DisplayName("Check if MST is undirected")
    void testMSTIsUndirected() {
        System.out.println("\n=== Test: Check if MST is undirected ===");
        Graph<String, TrackWeight> undirected = MST.convertToUndirected(simpleGraph);
        Graph<String, TrackWeight> mst = MST.kruskal(undirected);

        boolean isUndirected = !mst.isDirected();
        System.out.println("MST is undirected: " + isUndirected + " (expected: true)");
        assertFalse(mst.isDirected(), "Resulting MST graph should be undirected");
        System.out.println("✓ MST graph is undirected");
    }


}
