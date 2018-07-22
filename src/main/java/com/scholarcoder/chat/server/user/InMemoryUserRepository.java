package com.scholarcoder.chat.server.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private List<User> userList = new ArrayList<>();

    @Override
    public void add(String username) throws UserAlreadyExistsException {
        Optional<User> existingUser = userList.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
        if(existingUser.isPresent()) {
            throw new UserAlreadyExistsException("The user " + username + " already exists!");
        }
        this.userList.add(new User(username));
    }

    @Override
    public User findByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userList;
    }

    @Override
    public void deleteAll() {
        userList.clear();
    }
}
