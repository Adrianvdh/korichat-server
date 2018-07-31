package com.scholarcoder.chat.server.api.user.repository;

import com.scholarcoder.chat.server.api.user.UserAlreadyExistsException;

import java.util.List;

public interface UserRepository {

    void add(String username) throws UserAlreadyExistsException;

    User findByUsername(String username);

    List<User> findAll();

    void deleteAll();
}
