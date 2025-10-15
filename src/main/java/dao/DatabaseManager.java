package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/networks_db", "admin", "admin");
    }
}
