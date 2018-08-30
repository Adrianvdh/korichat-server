package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.store.session.Session;
import com.scholarcoder.chat.server.protocol.ChatRequest;
import com.scholarcoder.chat.server.protocol.ChatResponse;
import com.scholarcoder.chat.server.protocol.Responses;
import com.scholarcoder.chat.server.api.user.repository.User;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;

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
    public void doPerform(ChatRequest request, ChatResponse response) {
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
