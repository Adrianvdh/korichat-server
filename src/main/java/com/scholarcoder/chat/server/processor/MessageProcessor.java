package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.processor.commands.CommandHandler;
import com.scholarcoder.chat.server.processor.commands.CommandHandlersRegistry;
import com.scholarcoder.chat.server.transport.ChatRequest;
import com.scholarcoder.chat.server.transport.ChatResponse;
import com.scholarcoder.chat.server.transport.RequestService;

import java.util.List;

public class MessageProcessor {
    private List<CommandHandler> commandHandlers;

    public MessageProcessor() {
        this.commandHandlers = CommandHandlersRegistry.getRegisteredCommandHandlers();
    }

    public String process(String message) {
        RequestService requestService = new RequestService();
        ChatRequest chatRequest = requestService.parseRequestMessage(message);

        for (CommandHandler commandHandler : commandHandlers) {
            if(commandHandler.applicable(chatRequest.getMethod())) {
                ChatResponse chatResponse = new ChatResponse();

                commandHandler.doPerform(chatRequest, chatResponse);
                return chatResponse.asStringPayload();
            }
        }
        return "405 Command Not Allowed";
    }
}
