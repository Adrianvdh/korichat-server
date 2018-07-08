package com.scholarcoder.chat.server.user;

import java.util.List;

public interface UserRepository {

    void add(String username) throws UserAlreadyExistsException;

    User findByUsername(String username);

    List<User> findAll();

    void deleteAll();
}
