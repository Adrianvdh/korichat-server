package org.korichat.server.api.user.repository;

import org.korichat.server.repository.EmbeddedDatabaseBuilder;
import org.korichat.server.repository.HsqldbConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class UserRepositoryTest {

    Connection connection;
    SqlUserRepository userRepository;

    @Before
    public void setUp(){
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.configureConnection(HsqldbConnection.getInstance().getInprocessConnection());
        builder.addUpdateScript("hsqldb/create-schema.sql");
        this.connection = builder.build();

        userRepository = new SqlUserRepository(connection);
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {
        User user = new User("adrian");

        User savedUser = userRepository.save(user);
        Integer userId = savedUser.getUserId();

        User expectedUser = new User(userId, "adrian");

        Assert.assertEquals(expectedUser, savedUser);
    }

    @Test
    public void checkIfUserExists_userExists() {
        User user = new User("adrian");
        userRepository.save(user);

        Assert.assertTrue(userRepository.exists("adrian"));
    }


    @Test
    public void checkIfUserExists_userDoesntExists() {
        Assert.assertFalse(userRepository.exists("adrian"));
    }


    @Test
    public void testFindByUserName() {
        User savedUser = userRepository.save(new User("adrian"));

        User foundUser = userRepository.findByUsername("adrian");

        Assert.assertEquals(savedUser, foundUser);

    }

    @Test
    public void testFindAll() {
        User userAdrian = userRepository.save(new User("adrian"));
        User userJosie = userRepository.save(new User("josie"));
        User userJosh = userRepository.save(new User("josh"));
        List<User> savedUsers = Arrays.asList(userAdrian, userJosie, userJosh);

        List<User> foundUsers = userRepository.findAll();

        Assert.assertEquals(savedUsers, foundUsers);
    }

    @Test
    public void testDeleteAll() {
        userRepository.save(new User("adrian"));
        userRepository.save(new User("josie"));
        userRepository.save(new User("josh"));

        List<User> existingUsers = userRepository.findAll();
        Assert.assertFalse(existingUsers.isEmpty());

        userRepository.deleteAll();

        List<User> notExistingUsers = userRepository.findAll();
        Assert.assertTrue(notExistingUsers.isEmpty());
    }
}
