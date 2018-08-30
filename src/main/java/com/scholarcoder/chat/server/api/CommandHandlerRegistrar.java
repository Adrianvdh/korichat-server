package com.scholarcoder.chat.server.api;

import com.scholarcoder.chat.server.api.user.ListUserCommand;
import com.scholarcoder.chat.server.api.user.RegisterUserCommand;
import com.scholarcoder.chat.server.api.user.UseUserCommand;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;
import com.scholarcoder.chat.server.api.user.repository.UserRepositorySingleton;
import com.scholarcoder.chat.server.store.session.SessionStore;
import com.scholarcoder.chat.server.store.session.SessionStoreSingelton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
