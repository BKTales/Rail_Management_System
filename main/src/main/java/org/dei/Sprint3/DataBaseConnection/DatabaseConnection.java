package org.dei.Sprint3.DataBaseConnection;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getInstance() throws Exception {
        if (connection == null || connection.isClosed()) {
            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String pass = dotenv.get("DB_PASSWORD");

            connection = DriverManager.getConnection(url, user, pass);
        }
        return connection;
    }
}
