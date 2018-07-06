package com.pingpongchat.server.user;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryTest {

    @Test
    public void getUserRepository() {
        UserRepository userRepository = UserRepositorySingleton.get();

        Assert.assertTrue(userRepository != null);
    }


    @Test
    public void testAddUser() throws Exception {
        UserRepository userRepository = UserRepositorySingleton.get();
        String username = "adrian";

        userRepository.add(username);

        User user = userRepository.findByUsername(username);

        Assert.assertEquals(username, user.getUsername());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUserThatAlreadyExists() throws Exception {
        UserRepository userRepository = UserRepositorySingleton.get();
        String username = "adrian";

        userRepository.add(username);
        userRepository.add(username);
    }

    @Test
    public void testFindAllUsers() throws UserAlreadyExistsException {
        UserRepository userRepository = UserRepositorySingleton.get();
        userRepository.add("adrian");
        userRepository.add("josie");

        List<User> users = userRepository.findAll();
        List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());

        Assert.assertThat(usernames, Matchers.contains("adrian", "josie"));
    }

    @Test
    public void testDeleteAll() throws UserAlreadyExistsException {
        UserRepository userRepository = UserRepositorySingleton.get();
        userRepository.add("adrian");
        userRepository.add("josie");

        userRepository.deleteAll();

        Assert.assertTrue(userRepository.findAll().size() == 0);
    }
}
