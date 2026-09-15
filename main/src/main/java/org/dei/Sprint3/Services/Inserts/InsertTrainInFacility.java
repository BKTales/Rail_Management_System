package org.dei.Sprint3.Services.Inserts;

import org.dei._Time.TimeInSegment;

import java.sql.*;

public class InsertTrainInFacility {
    public static void run(Connection con, int facilityId, int trainId, TimeInSegment time) throws SQLException {
        CallableStatement cTrainInFacility = con.prepareCall("{ CALL addFacilityTrain(?, ?, ?, ?) }");
        // addFacilityTrain (p_facilityId NUMBER, p_trainId NUMBER, p_arriveTime TIMESTAMP, p_leaveTime TIMESTAMP)
        cTrainInFacility.setInt("facilityId", facilityId);
        cTrainInFacility.setInt("trainId", trainId);
        cTrainInFacility.setTimestamp("arriveTime", Timestamp.valueOf(time.getStartTime().toString()));
        cTrainInFacility.setTimestamp("leaveTime", Timestamp.valueOf(time.getStartTime().toString()));
        cTrainInFacility.execute();
        cTrainInFacility.close();
    }
}
