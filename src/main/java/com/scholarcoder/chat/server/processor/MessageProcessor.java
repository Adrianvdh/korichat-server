package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.processor.commands.CommandHandler;
import com.scholarcoder.chat.server.processor.commands.CommandHandlersRegistry;
import com.scholarcoder.chat.server.transport.ChatRequest;
import com.scholarcoder.chat.server.transport.ChatResponse;
import com.scholarcoder.chat.server.transport.RequestService;
import com.scholarcoder.chat.server.transport.ResponseService;

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

                ResponseService responseService = new ResponseService();
                String responseMessage = responseService.deserializeAsString(chatResponse);

                return responseMessage;
            }
        }
        return "405 Command Not Allowed";
    }
}
