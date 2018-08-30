package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.store.session.Session;
import com.scholarcoder.chat.server.store.session.SessionStore;
import com.scholarcoder.chat.server.protocol.ChatRequest;
import com.scholarcoder.chat.server.protocol.ChatResponse;
import com.scholarcoder.chat.server.protocol.Responses;
import com.scholarcoder.chat.server.api.user.repository.User;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;

public class UseUserCommand implements CommandHandler {

    private final String command = "USE";
    private UserRepository userRepository;
    private SessionStore sessionStore;

    public UseUserCommand(UserRepository userRepository, SessionStore sessionStore) {
        this.userRepository = userRepository;
        this.sessionStore = sessionStore;
    }

    @Override
    public boolean applicable(String action) {
        return command.equals(action);
    }

    @Override
    public void handle(ChatRequest request, ChatResponse response) {
        String username = request.getMetaData();

        User user = userRepository.findByUsername(username);
        if(user == null) {
            response.setStatusCode("400 Bad request");
        }

        Session session = new Session();
        session.setUsername(username);

        sessionStore.putSession(session);

        response.setStatusCode(Responses.OK);
        response.addCookie("SESSIONID", session.getSessionId());
    }
}
