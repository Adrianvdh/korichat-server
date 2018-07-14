package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.processor.commands.CommandHandler;
import com.scholarcoder.chat.server.processor.commands.CommandHandlersSingleton;

import java.util.List;

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
                String decoratedResponse = decorateResponse(response);
                return decoratedResponse;
            }
        }
        return "405 Command Not Allowed";
    }

    private String decorateResponse(String response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CHAT/1.0 " + response);


        return stringBuilder.toString();
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
