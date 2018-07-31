package com.scholarcoder.chat.server.api;

import com.scholarcoder.chat.server.api.user.ListUserCommand;
import com.scholarcoder.chat.server.api.user.RegisterUserCommand;
import com.scholarcoder.chat.server.api.user.UserRepositorySingleton;
import com.scholarcoder.chat.server.api.user.repository.UserRepository;
import com.scholarcoder.chat.server.store.session.SessionStore;
import com.scholarcoder.chat.server.store.session.SessionStoreSingelton;

import java.util.ArrayList;
import java.util.List;

public class CommandHandlersRegistry {

    private List<CommandHandler> commandHandlers;
    private static CommandHandlersRegistry instance;
    private CommandHandlersRegistry() {
    }

    public static List<CommandHandler> getRegisteredCommandHandlers() {
        if (instance == null) {
            instance = new CommandHandlersRegistry();
        }
        if(instance.commandHandlers == null) {
            instance.commandHandlers = registerCommandHandlers();
        }
        return instance.commandHandlers;
    }

    private static List<CommandHandler> registerCommandHandlers() {
        final UserRepository userRepository = getUserRepository();
        final SessionStore sessionStore = getSessionStore();

        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new RegisterUserCommand(userRepository));
        commandHandlers.add(new UseUserCommand(userRepository, sessionStore));
        commandHandlers.add(new ListUserCommand(userRepository));

        return commandHandlers;
    }

    private static SessionStore getSessionStore() {
        return SessionStoreSingelton.get();
    }

    private static UserRepository getUserRepository() {
        return UserRepositorySingleton.get();
    }
}
