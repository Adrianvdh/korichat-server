package com.scholarcoder.chat.server.api.user.repository;

import com.scholarcoder.chat.server.repository.EmbeddedDatabaseBuilder;
import com.scholarcoder.chat.server.repository.HsqldbConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepositoryTest {

    Connection connection;

    @Before
    public void setUp() throws SQLException {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.configureConnection(HsqldbConnection.getInstance().getInprocessConnection());
        builder.addUpdateScript("hsqldb/create-schema.sql");
        this.connection = builder.build();

        Statement statement = connection.createStatement();
        statement.executeUpdate("delete from PUBLIC.USER");
    }

    @Test
    public void testCreateUser() {
        SqlUserRepository userRepository = new SqlUserRepository(connection);
        User user = new User("adrian");

        User savedUser = userRepository.save(user);
        Integer userId = savedUser.getUserId();

        User expectedUser = new User(userId, "adrian");

        Assert.assertEquals(expectedUser, savedUser);
    }

    @Test
    public void testFindOne() {
        SqlUserRepository userRepository = new SqlUserRepository(connection);
        User savedUser = userRepository.save(new User("adrian"));

        User foundUser = userRepository.findOne(savedUser.getUserId());

        Assert.assertEquals(savedUser, foundUser);

    }
}
