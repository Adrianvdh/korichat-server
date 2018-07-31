package com.scholarcoder.chat.server.api.user.repository;

import com.scholarcoder.chat.server.repository.ConnectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class UserRepositoryTest {

    private Connection mySqlConnection;

    @Before
    public void setUp() {
        mySqlConnection = ConnectionUtil.getInstance().getMySqlConnection();
    }

    @Test
    public void testSaveUser() {
        SqlUserRepository userRepository = new SqlUserRepository(mySqlConnection);
        User user = new User("adrian");

        User savedUser = userRepository.save(user);
        Integer userId = savedUser.getUserId();

        User expectedUser = new User(userId, "adrian");

        Assert.assertEquals(expectedUser, savedUser);
    }

}
