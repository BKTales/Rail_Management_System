package org.dei;

import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.dei.Sprint3.JavaFX.LogisticsApplication; // <-- Onde vamos por a GUI
import javafx.application.Application;
import org.dei.Sprint3.Services.ResetDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- BOOTSTRAPING DATA ---");


        Bootstrap b = new Bootstrap();
        try {
//            Connection conn = DatabaseConnection.getInstance();
//
//            String call = "{call cleanLogisticsData()}";
//            try (java.sql.CallableStatement cs = conn.prepareCall(call)) {
//                cs.execute();
//            }
            b.bootProgram();
            System.out.println("Database and Graph loaded successfully.");
        } catch (Exception e) {
            System.err.println("\u001B[31m[CRITICAL ERROR] Failed to connect to Database.\u001B[0m");
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("--- LAUNCHING GUI ---");

        Application.launch(LogisticsApplication.class, args);
    }
}