package com.scholarcoder.chat.server.processor.commands;

import com.scholarcoder.chat.server.user.UserAlreadyExistsException;
import com.scholarcoder.chat.server.user.UserRepository;

public class RegisterUserCommand implements CommandHandler {

    private final String command = "REG";

    private UserRepository userRepository;

    public RegisterUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean applicable(String action) {
        return command.equals(action);
    }

    @Override
    public String doPerform(String payloadBody) {
        String username = payloadBody;
        try {
            userRepository.add(username);
            return "201 Created";

        } catch (UserAlreadyExistsException e) {
            return "409 Conflict";
        }
    }

}
