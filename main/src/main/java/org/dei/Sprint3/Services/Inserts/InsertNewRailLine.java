package org.dei.Sprint3.Services.Inserts;

import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertNewRailLine {
    public static void run(RailLine line, RailSegment firstSegment, Connection con) throws SQLException {
        CallableStatement cLines = con.prepareCall("{CALL addRailLine(?,?,?,?,?,?)}");
        // addRailLine (p_railLineId NUMBER, p_ownerId NVARCHAR2, p_startFacilityId NUMBER,
        //                  p_endFacilityId NUMBER, p_lineName NVARCHAR2, p_firstRailSegment NUMBER)
        cLines.setInt("p_railLineId", Integer.valueOf(line.getRailLineId()));
        cLines.setString("p_ownerId", line.getOwner());
        cLines.setInt("p_startFacilityId", line.getStartFacility().getId());
        cLines.setInt("p_endFacilityId", line.getStartFacility().getId());
        cLines.setString("p_lineName", line.getName());
        cLines.setInt("p_firstRailSegment", Integer.valueOf(firstSegment.getSegmentId()));
        cLines.execute();
        cLines.close();
    }
}
