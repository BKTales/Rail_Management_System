package org.dei;

import org.dei.Sprint3.DataBaseConnection.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    @DisplayName("Deve conectar à base de dados com sucesso")
    void testDatabaseConnection() {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance();

            assertNotNull(con, "the connection is null");
            assertTrue(con.isValid(2), "connection should be valid and active");

            System.out.println("✅ connection established!");

        } catch (Exception e) {
            fail("Fail to connect to the db: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    @DisplayName("Ler e Imprimir Locomotivas da Base de Dados")
    void testPrintDatabaseData() {
        try (Connection con = DatabaseConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT locomotiveId FROM Locomotive")) {

            System.out.println("\n=== DATA BASE, ALL LOCOMOTIVES ===");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("locomotiveId");

                System.out.println("Locomotive id ID: " + id);
            }

            if (!hasData) {
                System.out.println("Locomotive table exits but its empty!");
            }

            System.out.println("==================================================\n");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Fail to read the data: " + e.getMessage());
        }
    }
}
