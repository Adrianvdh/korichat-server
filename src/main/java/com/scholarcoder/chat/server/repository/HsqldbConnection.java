package com.scholarcoder.chat.server.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class HsqldbConnection {

    private static HsqldbConnection instance = new HsqldbConnection();

    private Connection inprocessConnection;
    private Connection remoteConnection;

    private Properties properties = new Properties();
    private HsqldbConnection() {
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
    }

    public static HsqldbConnection getInstance() {
        return instance;
    }

    public Connection getInprocessConnection() {
        if (inprocessConnection == null) {
            try {
                inprocessConnection = DriverManager.getConnection("jdbc:hsqldb:mem:korichatdb", "SA", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return inprocessConnection;
    }

    public Connection getRemoteConnection() {
        if (remoteConnection == null) {
            try {
                remoteConnection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return remoteConnection;
    }

}


