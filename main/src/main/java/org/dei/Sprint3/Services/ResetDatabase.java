package org.dei.Sprint3.Services;

import org.dei.Repository.FreightRepository;
import org.dei.Repository.TrainRepository;
import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import java.sql.Connection;

public class ResetDatabase {

    public static void cleanLogisticsData() throws Exception {
        Connection conn = DatabaseConnection.getInstance();

        String call = "{call cleanLogisticsData()}";
        try (java.sql.CallableStatement cs = conn.prepareCall(call)) {
            cs.execute();
        }
    }

    public static void cleanRepositories(){
        TrainRepository.getInstance().clearTrains();
        FreightRepository.getInstance().clear();
    }
}