package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;
import com.scholarcoder.chat.server.transport.ChatRequest;
import com.scholarcoder.chat.server.transport.ChatResponse;
import com.scholarcoder.chat.server.transport.Responses;

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
