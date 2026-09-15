package org.dei.Sprint3.Services.Inserts;

import org.dei._Train.Locomotive;
import org.dei._Train.Train;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InsertTrain {

        public static void run(Connection con, Train train) throws Exception {

        String callProcedure = "{call addTrain(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(callProcedure)) {
            cs.setString(1, train.getTrainId());
            cs.setInt(2, train.getRoute().getRouteId());

            cs.setDouble(3, train.getMaxLength());

            cs.setTimestamp(4, Timestamp.valueOf(train.getDepartureTime()));

            LocalDateTime arrival;
            if (train.getArrival() != null && train.getArrival().getArrivalTime() != null) {
                arrival = train.getArrival().getArrivalTime();
            } else if (train.getDepartureTime() != null && train.getRoute() != null) {
                arrival = train.calculateArrivalTime(train.getDepartureTime(), true);
            } else {
                arrival = train.getDepartureTime().plusHours(2);
            }
            cs.setTimestamp(5, Timestamp.valueOf(arrival));

            cs.execute();
        }

        // Use PL/SQL procedure to insert locomotive-train relationships
        String callLocoProcedure = "{call addLocomotiveToTrain(?, ?)}";
        try (CallableStatement cs = con.prepareCall(callLocoProcedure)) {
            if (train.getLocomotives() != null) {
                for (Locomotive l : train.getLocomotives()) {
                    cs.setString(1, train.getTrainId());
                    cs.setString(2, l.getNumber());
                    cs.execute();
                }
            }
        }
        
        // Update wagon locations when freights are delivered
        // When a train completes its journey, wagons should be at their destination facility
        // Update all freights on the train to their end facilities
        if (train.getFreightsOnTrain() != null && !train.getFreightsOnTrain().isEmpty()) {
            System.out.println("[LOG] Comboio " + train.getTrainId() + " agendado com " + 
                             train.getFreightsOnTrain().size() + " freight(s)");
            for (var freight : train.getFreightsOnTrain()) {
                if (freight.getEndFacility() != null && freight.getWagons() != null) {
                    try {
                        System.out.println("[LOG] A processar Freight " + freight.getId() + 
                                         " com " + freight.getWagons().size() + " wagons");
                        UpdateWagonLocation.updateWagonsForDeliveredFreight(con, freight);
                    } catch (SQLException e) {
                        System.err.println("Warning: Failed to update wagon locations for freight " + 
                                         freight.getId() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}