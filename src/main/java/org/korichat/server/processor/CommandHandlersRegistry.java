package org.korichat.server.processor;

import org.korichat.server.api.CommandHandler;
import org.korichat.server.api.CommandHandlerRegistrar;

import java.util.List;

public class CommandHandlersRegistry {
    private static CommandHandlersRegistry instance = new CommandHandlersRegistry();

    private List<CommandHandler> commandHandlers;

    private CommandHandlersRegistry() {
    }

    public static CommandHandlersRegistry getInstance() {
        return instance;
    }

    public List<CommandHandler> getRegisteredCommandHandlers() {
        if(commandHandlers == null) {
           commandHandlers = new CommandHandlerRegistrar().getCommandHandlers();
        }
        return commandHandlers;
    }

}
