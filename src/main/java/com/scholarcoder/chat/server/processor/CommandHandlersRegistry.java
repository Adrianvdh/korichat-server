package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.api.CommandHandlerRegistrar;

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
           commandHandlers = new CommandHandlerRegistrar().commandHandlers.get();
        }
        return commandHandlers;
    }

}
