package com.scholarcoder.chat.server.processor.commands;

import com.scholarcoder.chat.server.user.UserRepository;
import com.scholarcoder.chat.server.user.UserRepositorySingleton;

import java.util.ArrayList;
import java.util.List;

public class CommandHandlersSingleton {

    private List<CommandHandler> commandHandlers;
    private static CommandHandlersSingleton instance;
    private CommandHandlersSingleton() {
    }

    public static List<CommandHandler> getRegisteredCommandHandlers() {
        if (instance == null) {
            instance = new CommandHandlersSingleton();
        }
        if(instance.commandHandlers == null) {
            instance.commandHandlers = registerCommandHandlers();
        }
        return instance.commandHandlers;
    }

    private static List<CommandHandler> registerCommandHandlers() {
        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new RegisterUserCommand(getUserRepository()));
        commandHandlers.add(new ListUserCommand(getUserRepository()));
        return commandHandlers;
    }

    private static UserRepository getUserRepository() {
        return UserRepositorySingleton.get();
    }
}
