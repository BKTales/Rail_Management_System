package org.dei.Sprint3.Services.Inserts;

import org.dei._Train.Freight;
import org.dei._Train.Wagon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class to associate a Wagon with a Freight in the database.
 * Calls the PL/SQL procedure: addWagonToFreight(p_freightId, p_wagonId).
 */
public class InsertWagonInFreight {

    /**
     * Associates a wagon with a freight using PL/SQL.
     *
     * @param con     The active database connection.
     * @param freight The freight object (must have a valid ID).
     * @param wagon   The wagon object to attach (must have a valid ID).
     * @throws SQLException If the database interaction fails.
     */
    public static void run(Connection con, Freight freight, Wagon wagon) throws SQLException {
        // PL/SQL Procedure signature: addWagonToFreight(p_freightId, p_wagonId)
        String call = "{call addWagonToFreight(?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(call)) {
            // Parse IDs to Integer assuming DB uses NUMBER
            try {
                cstmt.setInt(1, Integer.parseInt(freight.getId()));
                cstmt.setInt(2, Integer.parseInt(wagon.getWagonId()));
            } catch (NumberFormatException e) {
                throw new SQLException("Invalid ID format for Freight or Wagon: " + e.getMessage());
            }

            cstmt.execute();
        }
    }
}