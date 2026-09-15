package org.dei.Sprint3.Services.Inserts;

import org.dei._Train.Freight;
import org.dei._Train.Wagon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Updates wagon locations in the database after a freight is delivered to its destination
 */
public class UpdateWagonLocation {
    
    /**
     * Updates all wagons in a freight to be at the freight's end facility
     * @param con Database connection
     * @param freight The freight that has been delivered
     * @throws SQLException if database operation fails
     */
    public static void updateWagonsForDeliveredFreight(Connection con, Freight freight) throws SQLException {
        if (freight == null || freight.getWagons() == null || freight.getWagons().isEmpty()) {
            return; // Nothing to update
        }
        
        if (freight.getEndFacility() == null) {
            return; // No destination facility
        }
        
        String startFacilityName = freight.getStartFacility() != null ? freight.getStartFacility().getName() : "Unknown";
        String endFacilityName = freight.getEndFacility().getName();
        int destinationFacilityId = freight.getEndFacility().getId();
        
        // Use PL/SQL procedure to update wagon locations
        String call = "{call updateWagonLocation(?, ?)}";
        try (CallableStatement cs = con.prepareCall(call)) {
            for (Wagon wagon : freight.getWagons()) {
                try {
                    cs.setInt(1, Integer.parseInt(wagon.getWagonId()));
                    cs.setInt(2, destinationFacilityId);
                    cs.execute();
                    System.out.println("[LOG] Wagon " + wagon.getWagonId() + " a ir de " + startFacilityName + " para " + endFacilityName);
                } catch (NumberFormatException e) {
                    // If wagonId is not a number, skip or handle differently
                    System.err.println("Warning: Wagon ID " + wagon.getWagonId() + " is not a number, skipping location update");
                    continue;
                }
            }
            System.out.println("[LOG] Freight " + freight.getId() + ": " + freight.getWagons().size() + 
                             " wagons movidos de " + startFacilityName + " para " + endFacilityName);
        }
    }
}

