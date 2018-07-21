package com.scholarcoder.chat.server.processor.commands;

import com.scholarcoder.chat.server.transport.ChatRequest;
import com.scholarcoder.chat.server.transport.ChatResponse;
import com.scholarcoder.chat.server.transport.Responses;
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
    public void doPerform(ChatRequest request, ChatResponse response) {
        String username = request.getMetaData();
        try {
            userRepository.add(username);
            response.setStatusCode(Responses.CREATED);

        } catch (UserAlreadyExistsException e) {
            response.setStatusCode(Responses.CONFLICT);
        }
    }
}
