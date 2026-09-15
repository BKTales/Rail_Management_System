package org.dei.Sprint3.Services.Inserts;

import org.dei._Path.Route;
import org.dei._Facilities.Facility;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class InsertNewRoute {
    public static void run(Connection con, Route route) {
        if (route == null || route.getPath() == null) {
            throw new RuntimeException("Error: Route or Path is null.");
        }

        int routeId = route.getRouteId();

        int startFacilityId = route.getPath().getStartFacility().getId();
        int endFacilityId = route.getPath().getEndFacility().getId();
        LocalDateTime departureTime = route.getDepartureDay();

        try {
            // Updated to include departure time (4th parameter)
            String sqlRoute = "{call writeRouteToDB(?, ?, ?, ?)}";

            try (CallableStatement stmt = con.prepareCall(sqlRoute)) {
                stmt.setInt(1, routeId);          // 1º routeId
                stmt.setInt(2, startFacilityId);  // 2º startFacilityId
                stmt.setInt(3, endFacilityId);    // 3º endFacilityId
                if (departureTime != null) {
                    stmt.setTimestamp(4, Timestamp.valueOf(departureTime)); // 4º departureTime
                } else {
                    stmt.setTimestamp(4, null); // null if no departure time
                }
                stmt.execute();
            }

            List<Facility> sequence = route.getPath().getRailFacilities();

            for (int i = 0; i < sequence.size(); i++) {
                int facilityId = sequence.get(i).getId();
                InsertNewRoutePoint.run(con, routeId, i + 1, facilityId);
            }

            System.out.println("[DB] Route " + routeId + " saved successfully.");

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting route into DB: " + e.getMessage(), e);
        }
    }
}