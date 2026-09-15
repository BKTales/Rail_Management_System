package org.dei.Sprint3.Services.Inserts;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertNewRoutePoint {
    public static void run(Connection con, int routeId, int order, int facilityId) throws SQLException {

        String sql = "{call writeRoutePointToDB(?, ?, ?)}";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, routeId);
            stmt.setInt(2, order);
            stmt.setInt(3, facilityId);

            stmt.execute();
        }
    }
}