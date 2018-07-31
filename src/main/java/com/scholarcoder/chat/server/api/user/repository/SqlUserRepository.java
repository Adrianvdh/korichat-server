package com.scholarcoder.chat.server.api.user.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUserRepository {

    private Connection connection;

    public SqlUserRepository(Connection connection) {
        this.connection = connection;
    }

    public User save(User user) {
        final String insertStatement = "insert into chatapp.User(Username) values ('" + user.getUsername() + "')";

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(insertStatement);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");

            if (resultSet.first()) {
                int userId = resultSet.getInt(1);

                return new User(userId, user.getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
