package org.korichat.server.api.user;

import org.korichat.server.api.CommandHandler;
import org.korichat.server.store.session.Session;
import org.korichat.server.store.session.SessionStore;
import org.korichat.server.protocol.ChatRequest;
import org.korichat.server.protocol.ChatResponse;
import org.korichat.server.protocol.Responses;
import org.korichat.server.api.user.repository.User;
import org.korichat.server.api.user.repository.UserRepository;

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
