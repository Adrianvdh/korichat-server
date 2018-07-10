package com.scholarcoder.chat.server.processor.commands;

import com.scholarcoder.chat.server.user.User;
import com.scholarcoder.chat.server.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListUserCommand implements CommandHandler {

    private final String command = "LISTUSER";
    private UserRepository userRepository;

    public ListUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean applicable(String action) {
        return command.equals(action);
    }

    @Override
    public String doPerform(String payloadBody) {
        List<User> users = userRepository.findAll();
        String userListString = users.stream().map(User::getUsername).collect(Collectors.joining(","));
        if(userListString.isEmpty()) {
            return "204 No Content";
        }
        return userListString;
    }
}
