package com.pingpongchat.server.user;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryTest {

    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = UserRepositorySingleton.get();
        userRepository.deleteAll();
    }

    @Test
    public void getUserRepository() {
        Assert.assertTrue(userRepository != null);
    }


    @Test
    public void testAddUser() throws Exception {
        String username = "adrian";

        userRepository.add(username);

        User user = userRepository.findByUsername(username);

        Assert.assertEquals(username, user.getUsername());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUserThatAlreadyExists() throws Exception {
        String username = "adrian";

        userRepository.add(username);
        userRepository.add(username);
    }

    @Test
    public void testFindAllUsers() throws UserAlreadyExistsException {
        userRepository.add("adrian");
        userRepository.add("josie");

        List<User> users = userRepository.findAll();
        List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());

        Assert.assertThat(usernames, Matchers.contains("adrian", "josie"));
    }

    @Test
    public void testDeleteAll() throws UserAlreadyExistsException {
        userRepository.add("adrian");
        userRepository.add("josie");

        userRepository.deleteAll();

        Assert.assertTrue(userRepository.findAll().size() == 0);
    }
}
