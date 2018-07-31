package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.server.api.user.User;
import com.scholarcoder.chat.server.api.user.UserAlreadyExistsException;
import com.scholarcoder.chat.server.api.user.UserRepository;
import com.scholarcoder.chat.server.api.user.UserRepositorySingleton;
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

    @Test
    public void testFindUserThatDoesnotExist() {
        String username = "someuser";

        User user = userRepository.findByUsername(username);

        Assert.assertNull(user);
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
