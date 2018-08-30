package org.korichat.server.api.user;

import org.korichat.server.api.CommandHandler;
import org.korichat.server.api.user.repository.User;
import org.korichat.server.api.user.repository.UserRepository;
import org.korichat.server.protocol.ChatRequest;
import org.korichat.server.protocol.ChatResponse;
import org.korichat.server.protocol.Responses;

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
