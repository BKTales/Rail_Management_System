package org.dei.Sprint3.Services.Inserts;

import org.dei._Facilities.Facility;
import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;
import org.dei._Time.TimeInSegment;
import org.dei._Train.Train;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class InsertScheduleData {

    public static void saveDetailedSchedule(Connection con, Train train) throws SQLException {
        if (train.getCalculatedSegmentTimes() == null || train.getCalculatedSegmentTimes().isEmpty()) return;

        List<Facility> facilities = train.getRoute().getPath().getRailFacilities();
        List<TimeInSegment> times = train.getCalculatedSegmentTimes();

        int timeIndex = 0;

        // 1. Primeira Estação
        if (timeIndex < times.size()) {
            insertFacilityTrain(con, facilities.get(0), train, times.get(timeIndex));
            timeIndex++;
        }

        for (int i = 0; i < facilities.size() - 1; i++) {
            Facility f1 = facilities.get(i);
            Facility f2 = facilities.get(i + 1);
            RailLine line = f1.getConnections().get(f2);

            // 2. Segmentos
            if (line != null) {
                for (RailSegment seg : line.getRailSegments()) {
                    if (timeIndex < times.size()) {
                        insertTimeInSegment(con, seg, train, times.get(timeIndex));
                        timeIndex++;
                    }
                }
            }

            // 3. Próxima Estação
            if (timeIndex < times.size()) {
                insertFacilityTrain(con, f2, train, times.get(timeIndex));
                timeIndex++;
            }
        }
    }

    private static void insertFacilityTrain(Connection con, Facility f, Train t, TimeInSegment time) throws SQLException {
        // Use PL/SQL procedure to insert facility-train relationship
        String call = "{call addFacilityTrain(?, ?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(call)) {
            cs.setInt(1, f.getId());
            cs.setInt(2, Integer.parseInt(t.getTrainId()));
            cs.setTimestamp(3, Timestamp.valueOf(time.getStartTime()));
            cs.setTimestamp(4, Timestamp.valueOf(time.getEndTime()));
            cs.execute();
        }
    }

    private static void insertTimeInSegment(Connection con, RailSegment seg, Train t, TimeInSegment time) throws SQLException {
        // Use PL/SQL procedure to insert time in segment
        String call = "{call addTimeInSegment(?, ?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(call)) {
            cs.setInt(1, Integer.parseInt(seg.getSegmentId()));
            cs.setInt(2, Integer.parseInt(t.getTrainId()));
            cs.setTimestamp(3, Timestamp.valueOf(time.getStartTime()));
            cs.setTimestamp(4, Timestamp.valueOf(time.getEndTime()));
            cs.execute();
        }
    }
}