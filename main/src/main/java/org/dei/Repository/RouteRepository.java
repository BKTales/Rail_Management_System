package org.dei.Repository;

import org.dei.Sprint3.DataBaseConnection.DatabaseConnection; // Ensure this import is correct for your project structure
import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._Train.Freight; // Needed for the empty list

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RouteRepository {
    private static RouteRepository instance = null;
    private ArrayList<Route> routes;

    public RouteRepository() {
        routes = new ArrayList<>();
        loadRoutesFromDB();
    }

    public static RouteRepository getInstance() {
        if (instance == null) {
            instance = new RouteRepository();
        }
        return instance;
    }

    public Route getRoute(int index) {
        if (index >= 0 && index < routes.size()) {
            return routes.get(index);
        }
        return null;
    }

    public Route getRouteByIdentifier(int routeIdentifier) {
        for (Route route : routes) {
            if (route.getRouteId() == routeIdentifier) {
                return route;
            }
        }
        return null;
    }

    public List<Route> getAllRoutes() {
        return this.routes;
    }

    public List<Route> getValidRoutes() {
        LocalDateTime now = LocalDateTime.now();
        List<Route> validRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (!route.getDepartureDay().isBefore(now)) {
                validRoutes.add(route);
            }
        }
        return validRoutes;
    }

    public void loadRoutesFromDB() {
        this.routes.clear();
        Connection conn = null;
        PreparedStatement stmtRoute = null;
        PreparedStatement stmtPoints = null;

        try {
            conn = DatabaseConnection.getInstance();

            String sqlRoute = "SELECT routeId, departureDate FROM Route";
            stmtRoute = conn.prepareStatement(sqlRoute);
            ResultSet rsRoute = stmtRoute.executeQuery();

            while (rsRoute.next()) {
                System.out.println(rsRoute.getString("routeId"));
                int routeId = rsRoute.getInt("routeId");
                java.sql.Timestamp departureTimestamp = rsRoute.getTimestamp("departureDate");
                LocalDateTime departureTime = null;
                if (departureTimestamp != null) {
                    departureTime = departureTimestamp.toLocalDateTime();
                }

                String sqlPoints =
                        "SELECT rp.\"order\", rp.facilityId " +
                                "FROM Route_Point rp " +
                                "WHERE rp.routeId = ? " +
                                "ORDER BY rp.\"order\" ASC";

                stmtPoints = conn.prepareStatement(sqlPoints);
                stmtPoints.setInt(1, routeId);
                ResultSet rsPoints = stmtPoints.executeQuery();

                List<Facility> stops = new ArrayList<>();

                while (rsPoints.next()) {
                    int facId = rsPoints.getInt("facilityId");
                    Facility f = FacilityRepository.getInstance().getFacility(facId);
                    if (f != null) {
                        stops.add(f);
                    }
                }
                rsPoints.close();
                stmtPoints.close();

                if (!stops.isEmpty()) {
                    Facility start = stops.get(0);
                    Facility end = stops.get(stops.size() - 1);

                    String realPathId = String.valueOf(routeId);

                    Path path = new Path(stops, start, end, realPathId);

                    // Use departureTime loaded from database, or null if not available
                    Route route = new Route(path, routeId, new ArrayList<Freight>(), departureTime);
                    this.routes.add(route);
                }
            }
            rsRoute.close();

            System.out.println("[DB LOAD] Loaded " + this.routes.size() + " routes from database.");

        } catch (SQLException e) {
            System.err.println("Error loading routes: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(stmtPoints != null) stmtPoints.close(); } catch(Exception e){}
            try { if(stmtRoute != null) stmtRoute.close(); } catch(Exception e){}
        }
    }
    /**
     * Gets the number of routes in the repository
     * @return the number of routes
     */
    public int size() {
        return routes.size();
    }


    /**
     * Removes all routes from the repository
     */
    public void clear() {
        routes.clear();
    }
}