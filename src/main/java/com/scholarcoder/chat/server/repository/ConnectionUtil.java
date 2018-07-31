package com.scholarcoder.chat.server.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static ConnectionUtil instance = new ConnectionUtil();

    private Connection connection;

    private ConnectionUtil() { }

    public static ConnectionUtil getInstance() {
        return instance;
    }

    public Connection getMySqlConnection() {
        if(connection == null) {
            connection = createConnection();
        }
        return connection;
    }

    private Connection createConnection() {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "root");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", connectionProps);
            connection.setCatalog("mysql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
