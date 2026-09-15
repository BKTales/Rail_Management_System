package org.dei.Sprint3.Services.CreateManualRoute;

import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.Services.Inserts.InsertNewRoute;
import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._RailLineNetwork.RailLine;
import org.dei._Path.Route;
import org.dei.Repository.GraphRepository;
import org.dei.Repository.RouteRepository;
import org.dei.Sprint3.Services.CreateRoute;
import org.dei.Sprint3.Services.GraphInitializer;
import org.dei.Sprint3.Services.GenerateRouteID;
import org.dei._Train.Freight;
import org.dei.Sprint3.Graph.RailNetworkGraphs.FacilityVertex;
import org.dei.Sprint3.Graph.RailNetworkGraphs.TrackWeightWithLine;
import org.dei.Sprint3.Graph.map.MapGraph;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CreateRouteController {

    private final HashMap<String, Path> savedPaths = new HashMap<>();
    private final GraphRepository graphRepository;

    public CreateRouteController() {
        this.graphRepository = GraphRepository.getInstance();
        ensureGraphLoaded();
    }

    private void ensureGraphLoaded() {
        if (graphRepository.getGraph() == null) {
            try {
                Connection con = DatabaseConnection.getInstance();
                MapGraph<FacilityVertex, TrackWeightWithLine> graph = GraphInitializer.createGraph(con);
                graphRepository.setGraph(graph);
            } catch (SQLException e) {
                System.err.println("Failed to load Graph: " + e.getMessage());
                graphRepository.setGraph(new MapGraph<>(false));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Path createAutomaticPath(String startName, String endName) {
        MapGraph<FacilityVertex, TrackWeightWithLine> graph = graphRepository.getGraph();

        FacilityVertex vStart = getVertexByName(startName);
        FacilityVertex vEnd = getVertexByName(endName);

        if (vStart == null || vEnd == null) throw new IllegalArgumentException("Stations not found in graph.");

        Path path = CreateRoute.generateAutomaticPath(graph, vStart, vEnd);

        if (path == null || path.getRailFacilities() == null || path.getRailFacilities().isEmpty()) {
            throw new IllegalArgumentException("No connection found between " + startName + " and " + endName);
        }

        String uniqueMapKey = "AUTO-" + System.currentTimeMillis();
        savedPaths.put(uniqueMapKey, path);

        return path;
    }



    public Path createIsolatedPath(List<String> stationSequence) {
        Path path = generatePathFromNames(stationSequence);
        String pathId = "P-" + System.currentTimeMillis();
        savedPaths.put(pathId, path);
        return path;
    }

    public void createRouteFromExistingPath(String pathId, LocalDateTime departureTime) {
        Path existingPath = savedPaths.get(pathId);
        if (existingPath == null) {
            throw new IllegalArgumentException("Path ID not found: " + pathId);
        }
        createAndSaveRoute(existingPath, new ArrayList<>(), departureTime);
    }

    public List<String> getReachableStations(String originName) {
        List<String> reachable = new ArrayList<>();
        FacilityVertex startNode = getVertexByName(originName);

        if (startNode == null) return reachable;

        // Algoritmo simples de busca (Breadth-First Search) para encontrar tudo o que está conectado
        Set<FacilityVertex> visited = new HashSet<>();
        Queue<FacilityVertex> queue = new LinkedList<>();

        visited.add(startNode);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            FacilityVertex current = queue.poll();

            // Adiciona à lista (menos o próprio nó de origem)
            if (!current.equals(startNode)) {
                reachable.add(current.getStation().getName());
            }

            for (FacilityVertex neighbor : graphRepository.getGraph().adjVertices(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        Collections.sort(reachable);
        return reachable;
    }

    public List<String> getDirectConnections(String stationName) {
        List<String> connections = new ArrayList<>();
        FacilityVertex v = getVertexByName(stationName);

        if (v != null) {
            // Percorre as arestas de saída deste vértice
            for (FacilityVertex neighbor : graphRepository.getGraph().adjVertices(v)) {
                connections.add(neighbor.getStation().getName());
            }
        }
        Collections.sort(connections);
        return connections;
    }

    public void createRouteWithNewPath(List<String> stationSequence, LocalDateTime departureTime) {
        Path newPath = generatePathFromNames(stationSequence);
        createAndSaveRoute(newPath, new ArrayList<>(), departureTime);
    }

    public void createAndSaveRoute(Path path, ArrayList<Freight> freights, LocalDateTime departureTime) {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance();


            int routeId = GenerateRouteID.run(con);


            Route route = new Route(path, routeId, freights, departureTime);

            RouteRepository.getInstance().loadRoutesFromDB();

            System.out.println("Saving Route " + routeId + " to database...");
            InsertNewRoute.run(con, route);
            System.out.println("[SUCCESS] Route saved to DB.");

        } catch (Exception e) {
            System.err.println("Failed to save route to DB: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("DB Error: " + e.getMessage());
        }
    }

    // --- Helpers ---

    private Path generatePathFromNames(List<String> stationSequence) {
        if (stationSequence == null || stationSequence.size() < 2)
            throw new IllegalArgumentException("Path must have at least 2 stations.");

        Facility start = getVertexByName(stationSequence.get(0)).getStation();
        Facility end = getVertexByName(stationSequence.get(stationSequence.size() - 1)).getStation();

        List<Facility> list = new ArrayList<>();
        for (String name : stationSequence) {
            FacilityVertex v = getVertexByName(name);
            if (v == null) throw new IllegalArgumentException("Station not found: " + name);
            list.add(v.getStation());
        }
        return start.createPath(list, start, end);
    }

    public HashMap<Facility, RailLine> getConnections(String stationName) {
        HashMap<Facility, RailLine> map = new HashMap<>();
        FacilityVertex v = getVertexByName(stationName);

        if (v != null) {
            for (FacilityVertex adj : graphRepository.getGraph().adjVertices(v)) {
                TrackWeightWithLine edgeWeight = graphRepository.getGraph().edge(v, adj).getWeight();

                if (edgeWeight != null && edgeWeight.getLine() != null) {
                    map.put(adj.getStation(), edgeWeight.getLine());
                }
            }
        }
        return map;
    }

    public FacilityVertex getVertexByName(String name) {
        if (name == null || graphRepository.getGraph() == null) return null;
        for (FacilityVertex v : graphRepository.getGraph().vertices()) {
            if (v.getStation().getName().equalsIgnoreCase(name)) return v;
        }
        return null;
    }

    public List<String> getAllStationNames() {
        List<String> names = new ArrayList<>();
        if (graphRepository.getGraph() == null) return names;
        for (FacilityVertex v : graphRepository.getGraph().vertices()) names.add(v.getStation().getName());
        Collections.sort(names);
        return names;
    }

    public boolean hasConnections(String stationName) {
        FacilityVertex v = getVertexByName(stationName);
        return v != null && graphRepository.getGraph().outDegree(v) > 0;
    }

    public List<String> getConnectedStationNames(String stationName) {
        List<String> names = new ArrayList<>();
        FacilityVertex v = getVertexByName(stationName);
        if (v != null) {
            for (FacilityVertex adj : graphRepository.getGraph().adjVertices(v)) {
                names.add(adj.getStation().getName());
            }
        }
        return names;
    }

    public Map<String, Path> getSavedPaths() { return savedPaths; }
    public List<Route> getAllRoutes() { return RouteRepository.getInstance().getAllRoutes(); }
    private int generateRouteId() { return (RouteRepository.getInstance().size() + 1); }


    public boolean isStationIsolated(String stationName) {
        return !hasConnections(stationName);
    }
}