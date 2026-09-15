package org.dei.Sprint3.Services.Inserts;

import org.dei._Train.Freight;
import org.dei._Train.Wagon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertNewFreight {

    public static void run(Connection con, Freight freight) throws SQLException {
        // Use PL/SQL procedure to insert freight
        String callFreight = "{call addFreight(?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(callFreight)) {
            cs.setInt(1, Integer.parseInt(freight.getId()));
            cs.setInt(2, freight.getStartFacility().getId());
            cs.setInt(3, freight.getEndFacility().getId());
            cs.execute();
        }
    }
}