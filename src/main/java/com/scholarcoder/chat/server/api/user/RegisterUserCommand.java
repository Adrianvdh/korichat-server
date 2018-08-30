package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.api.user.repository.User;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;
import com.scholarcoder.chat.server.protocol.ChatRequest;
import com.scholarcoder.chat.server.protocol.ChatResponse;
import com.scholarcoder.chat.server.protocol.Responses;

public class  RegisterUserCommand implements CommandHandler {

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
    public void handle(ChatRequest request, ChatResponse response) {
        String username = request.getMetaData();
        if(userRepository.exists(username)) {
            response.setStatusCode(Responses.CONFLICT);
            return;
        }

        userRepository.save(new User(username));
        response.setStatusCode(Responses.CREATED);
    }
}
