package com.example.GrpSB.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;

    private DbConnection() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "password");
    }

    static {
        try {
            instance = new DbConnection();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }

    public static DbConnection getInstance(){
        return instance;
    }

    public Connection getConnection()  {
        return connection;
    }
}
