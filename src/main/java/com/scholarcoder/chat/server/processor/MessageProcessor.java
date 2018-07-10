package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.processor.commands.CommandHandler;
import com.scholarcoder.chat.server.processor.commands.CommandHandlersSingleton;
import com.scholarcoder.chat.server.processor.commands.ListUserCommand;
import com.scholarcoder.chat.server.processor.commands.RegisterUserCommand;
import com.scholarcoder.chat.server.user.User;
import com.scholarcoder.chat.server.user.UserAlreadyExistsException;
import com.scholarcoder.chat.server.user.UserRepository;
import com.scholarcoder.chat.server.user.UserRepositorySingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageProcessor {
    private List<CommandHandler> commandHandlers;

    public MessageProcessor() {
        this.commandHandlers = CommandHandlersSingleton.getRegisteredCommandHandlers();
    }

    public String process(String message) {
        Payload payload = Payload.fromMessage(message);
        for (CommandHandler commandHandler : commandHandlers) {
            if(commandHandler.applicable(payload.command)) {
                String response = commandHandler.doPerform(payload.body);
                return response;
            }
        }
        return "405 Command Not Allowed";
    }



    static class Payload {
        public String command;
        public String body;

        public Payload(String command, String body) {
            this.command = command;
            this.body = body;
        }

        public static Payload fromMessage(String message) {

            String[] payloadString = message.split(" ");
            String command;
            if(payloadString.length == 1) {
                command = payloadString[0];
                return new Payload(command, "");
            }
            command = payloadString[0];
            String body = payloadString[1];

            return new Payload(command, body);
        }
    }
}
