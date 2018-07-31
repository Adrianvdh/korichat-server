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

public class CommandHandlersRegistry {
    private static CommandHandlersRegistry instance = new CommandHandlersRegistry();

    private List<CommandHandler> commandHandlers;

    private CommandHandlersRegistry() {
    }

    public static CommandHandlersRegistry getInstance() {
        return instance;
    }

    private List<CommandHandler> registerCommandHandlers() {
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
        return UserRepositorySingleton.get();
    }


    public List<CommandHandler> getRegisteredCommandHandlers() {
        if(commandHandlers == null) {
           commandHandlers = registerCommandHandlers();
        }
        return commandHandlers;
    }

}
