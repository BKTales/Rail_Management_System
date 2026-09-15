package org.dei.Sprint3.Graph.RailNetworkGraphs;

import org.dei.Sprint3.Graph.map.MapGraph;
import org.dei.Sprint3.Graph.matrix.MatrixGraph;
import org.dei._Facilities.Station.Station;
import java.util.HashMap;
import java.util.Map;

/**
 * Service layer for rail network graph operations.
 * Maintains dual representations (MapGraph + MatrixGraph) in sync.
 * Enforces business rules for stations and rail lines.
 */
public class RailNetworkGraphService {

    private MapGraph<StationVertex, TrackWeight> mapGraph;
    private MatrixGraph<StationVertex, TrackWeight> matrixGraph;

    // Map to quickly lookup StationVertex by Station ID
    private Map<Integer, StationVertex> stationIndex;

    public RailNetworkGraphService() {
        // Directed graph: departure -> arrival is meaningful
        this.mapGraph = new MapGraph<>(true);
        this.matrixGraph = new MatrixGraph<>(true);
        this.stationIndex = new HashMap<>();
    }

    /**
     * Adds a station to both graph representations.
     * Business rules:
     * - Station cannot be null
     * - Coordinates must be valid (non-null CartesianCoordinates)
     * - No duplicate stations (by ID)
     *
     * @param station Station object (contains name, id, lat/lon)
     * @param coordX X coordinate for SVG/Graphviz
     * @param coordY Y coordinate for SVG/Graphviz
     * @return true if added successfully, false if already exists or invalid
     */
    public boolean addStation(Station station, double coordX, double coordY) {
        if (station == null) {
            return false;
        }
        int stationId = station.getId();

        if (stationIndex.containsKey(stationId)) {
            return false;
        }

        CartesianCoordinates coords = new CartesianCoordinates(coordX, coordY);
        StationVertex vertex = new StationVertex(coords, station);

        boolean addedToMap = mapGraph.addVertex(vertex);
        boolean addedToMatrix = matrixGraph.addVertex(vertex);

        if (addedToMap && addedToMatrix) {
            stationIndex.put(stationId, vertex);
            return true;
        }

        // Rollback if one failed
        if (addedToMap) mapGraph.removeVertex(vertex);
        if (addedToMatrix) matrixGraph.removeVertex(vertex);

        return false;
    }

    /**
     * Adds a rail line (edge) between two stations.
     * Business rules:
     * - Both stations must exist in graph
     * - Line cannot already exist (no duplicate edges)
     * - Weight (distance, capacity, cost) must be valid
     *
     * @param departureStationId ID of departure station
     * @param arrivalStationId ID of arrival station
     * @param distance Distance in km
     * @param capacity Maximum trains per day
     * @param cost Metric combining distance, capacity, congestion, etc.
     * @return true if line added successfully, false if invalid or already exists
     */
    public boolean addLine(int departureStationId, int arrivalStationId,
                           double distance, double capacity, double cost) {

        StationVertex departureVertex = stationIndex.get(departureStationId);
        StationVertex arrivalVertex = stationIndex.get(arrivalStationId);

        if (departureVertex == null) {
            return false;
        }

        if (arrivalVertex == null) {
            return false;
        }

        if (mapGraph.edge(departureVertex, arrivalVertex) != null) {
            return false;
        }

        if (distance < 0 || capacity < 0) {
            return false;
        }

        TrackWeight weight = new TrackWeight(cost, distance, capacity);

        boolean addedToMap = mapGraph.addEdge(departureVertex, arrivalVertex, weight);
        boolean addedToMatrix = matrixGraph.addEdge(departureVertex, arrivalVertex, weight);

        if (addedToMap && addedToMatrix) {
            return true;
        }

        // Rollback
        if (addedToMap) mapGraph.removeEdge(departureVertex, arrivalVertex);
        if (addedToMatrix) matrixGraph.removeEdge(departureVertex, arrivalVertex);

        System.err.println("Error: Failed to add line to graph representations");
        return false;
    }

    public MapGraph<StationVertex, TrackWeight> getMapGraph() {
        return mapGraph;
    }

    public MatrixGraph<StationVertex, TrackWeight> getMatrixGraph() {
        return matrixGraph;
    }

    public Map<Integer, StationVertex> getStationIndex() {
        return stationIndex;
    }

    public int getStationCount() {
        return mapGraph.numVertices();
    }

    public int getLineCount() {
        return mapGraph.numEdges();
    }

    public StationVertex findStationById(int stationId) {
        return stationIndex.get(stationId);
    }
}
