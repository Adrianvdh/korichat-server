package org.korichat.server.api.user;

import org.korichat.server.api.CommandHandler;
import org.korichat.server.store.session.Session;
import org.korichat.server.protocol.ChatRequest;
import org.korichat.server.protocol.ChatResponse;
import org.korichat.server.protocol.Responses;
import org.korichat.server.api.user.repository.User;
import org.korichat.server.api.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListUserCommand implements CommandHandler {

    private UserRepository userRepository;

    public ListUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean applicable(String action) {
        return "LIST_USER".equals(action);
    }

    @Override
    public void handle(ChatRequest request, ChatResponse response) {
        Session session = request.getSession();
        if(session == null) {
            response.setStatusCode("401 Unauthorized");
            return;
        }

        List<User> users = userRepository.findAll();
        String userListString = users.stream().map(User::getUsername).collect(Collectors.joining(","));
        if(userListString.isEmpty()) {
            response.setStatusCode(Responses.NO_CONTENT);
        }
        response.setStatusCode(Responses.OK);
        response.setBody(userListString);
    }
}
