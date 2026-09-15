package org.dei.GraphTests.MSTRelatedTests;


import org.dei.Sprint3.Graph.ParserBerlgianNW;
import org.dei.Sprint3.GraphAlgorithms.MST;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Nested
@DisplayName("Real Belgian Network Integration Tests")
class RealNetworkTests {

    private RailNetworkGraphService graphService;
    private ParserBerlgianNW parser;
    private final String OUTPUT_FILENAME = "src/main/resources/belgium_backbone_test";

    private final String STATIONS_FILE = "src/main/resources/ Belgian_Rail_Network/stations.csv";
    private final String LINES_FILE = "src/main/resources/ Belgian_Rail_Network/lines.csv";

    @BeforeEach
    void setUpRealNetwork() {
        cleanUp();
        graphService = new RailNetworkGraphService();
        parser = new ParserBerlgianNW(graphService);

        // Load real network
        boolean loaded = parser.loadRailNetwork(STATIONS_FILE, LINES_FILE);
        assertTrue(loaded, "Should load real network successfully");
        assertTrue(graphService.getStationCount() > 0, "Should have loaded stations");
    }

    void cleanUp() {
        deleteFileIfExists(OUTPUT_FILENAME + ".dot");
        deleteFileIfExists(OUTPUT_FILENAME + ".svg");
    }

    private void deleteFileIfExists(String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    @DisplayName("Generate DOT/SVG for Full Belgium Network MST")
    void testGenerateFullNetworkBackbone() throws IOException {
        // 1. Get original directed graph
        var directedGraph = graphService.getMapGraph();

        // 2. Convert to Undirected
        var undirectedGraph = MST.convertToUndirected(directedGraph);
        assertFalse(undirectedGraph.isDirected());

        // 3. Compute MST (Kruskal)
        var mstGraph = MST.kruskal(undirectedGraph);


        // check same number of vertices
        assertEquals(undirectedGraph.numVertices(), mstGraph.numVertices(),
                "MST must include all stations from original network");

        // For a fully connected graph, edges = (V-1)*2.
        // However, real network might have disconnected components.
        // We just check if we reduced edges significantly compared to original
        assertTrue(mstGraph.numEdges() < undirectedGraph.numEdges(),
                "MST should have fewer edges than the original full mesh");

        MST.exportToGraphviz(mstGraph, OUTPUT_FILENAME);

        File dotFile = new File(OUTPUT_FILENAME + ".dot");
        assertTrue(dotFile.exists(), "DOT file must be created");
        assertTrue(dotFile.length() > 0, "DOT file must not be empty");

        String content = Files.readString(Path.of(dotFile.getPath()));

        assertTrue(content.contains("graph minimal_backbone"), "Must contain graph header");
        assertTrue(content.contains("node [shape=point"), "Must configure nodes");

        // Check if real stations are present (e.g. ID 6)
        assertTrue(content.contains("\"6\" [pos="), "Must contain station ID 6 with coordinates");

        // Count edges in DOT file (lines with "--")
        long edgesInDot = content.lines().filter(line -> line.contains("--")).count();

        // Since MapGraph stores 2 edges per connection (A->B, B->A), but DOT only needs 1 (A--B),
        // edgesInDot should be mstGraph.numEdges() / 2
        assertEquals(mstGraph.numEdges() / 2, edgesInDot,
                "DOT file should have exactly 1 line per undirected connection (half of directed edges)");

        File svgFile = new File(OUTPUT_FILENAME + ".svg");
        if (svgFile.exists()) {
            assertTrue(svgFile.length() > 1000, "SVG should have reasonable size");
            System.out.println("✓ SVG generated successfully");
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    System.out.println("Opening SVG visualization...");
                    java.awt.Desktop.getDesktop().open(svgFile);
                } catch (IOException e) {
                    System.err.println("Could not open SVG file automatically: " + e.getMessage());
                }
            }
        } else {
            System.out.println("⚠ SVG not generated (Graphviz 'neato' likely missing), but DOT test passed.");
        }
    }

}
