package org.korichat.server.api;

import org.korichat.server.api.user.ListUserCommand;
import org.korichat.server.api.user.RegisterUserCommand;
import org.korichat.server.api.user.UseUserCommand;
import org.korichat.server.api.user.repository.UserRepository;
import org.korichat.server.api.user.repository.UserRepositorySingleton;
import org.korichat.server.store.session.SessionStore;
import org.korichat.server.store.session.SessionStoreSingelton;

import java.util.ArrayList;
import java.util.List;

public class CommandHandlerRegistrar {

    public List<CommandHandler> getCommandHandlers() {
        final UserRepository userRepository = getUserRepository();
        final SessionStore sessionStore = getSessionStore();

        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new RegisterUserCommand(userRepository));
        commandHandlers.add(new UseUserCommand(userRepository, sessionStore));
        commandHandlers.add(new ListUserCommand(userRepository));

        return commandHandlers;
    }

    private SessionStore getSessionStore() {
        return SessionStoreSingelton.get();
    }

    private UserRepository getUserRepository() {
        return UserRepositorySingleton.getInstance().get();
    }


}
