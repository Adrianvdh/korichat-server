package com.scholarcoder.chat.client.processor.commands;

import com.scholarcoder.chat.client.store.session.Session;
import com.scholarcoder.chat.client.transport.ChatRequest;
import com.scholarcoder.chat.client.transport.ChatResponse;
import com.scholarcoder.chat.client.transport.Responses;
import com.scholarcoder.chat.client.user.User;
import com.scholarcoder.chat.client.user.UserRepository;

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
