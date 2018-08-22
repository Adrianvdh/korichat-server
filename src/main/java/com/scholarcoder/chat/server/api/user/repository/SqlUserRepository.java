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
        final String insertStatement = "insert into PUBLIC.User(USERNAME) values ('" + user.getUsername() + "')";

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(insertStatement);
            ResultSet resultSet = statement.executeQuery("select USERID from PUBLIC.USER where USERNAME = '" + user.getUsername() + "'");

            if (resultSet.next()) {
                int userId = resultSet.getInt(1);

                return new User(userId, user.getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findOne(Integer id) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from PUBLIC.USER where USERID = '" + id + "'");

            if (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String username = resultSet.getString(2);
                return new User(userId, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
