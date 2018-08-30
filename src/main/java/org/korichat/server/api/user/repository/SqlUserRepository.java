package org.korichat.server.api.user.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlUserRepository implements UserRepository {

    private Logger logger = LoggerFactory.getLogger(SqlUserRepository.class);

    private Connection connection;

    public SqlUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
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
            logger.error("SQLException occurred", e);
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from PUBLIC.USER where USERNAME = '" + username+ "'");

            if (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String fUsername = resultSet.getString(2);
                return new User(userId, fUsername);
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred", e);
        }
        return null;
    }

    @Override
    public boolean exists(String username) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from PUBLIC.USER where USERNAME = '" + username+ "'");

            return resultSet.next();
        } catch (SQLException e) {
            logger.error("SQLException occurred", e);
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from PUBLIC.USER");

            ArrayList<User> users = new ArrayList<>(resultSet.getFetchSize());
            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String username = resultSet.getString(2);

                users.add(new User(userId, username));
            }
            return users;
        } catch (SQLException e) {
            logger.error("SQLException occurred", e);
        }
        return null;
    }

    @Override
    public void deleteAll() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from PUBLIC.USER");
        } catch (SQLException e) {
            logger.error("SQLException occurred", e);
        }
    }
}