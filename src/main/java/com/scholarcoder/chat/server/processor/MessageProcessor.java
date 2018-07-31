package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.api.CommandHandlersRegistry;
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
        ChatRequest chatRequest = requestService.deserializeRequestMessage(message);

        ChatResponse chatResponse = new ChatResponse();
        boolean foundApplicableHandler = false;
        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.applicable(chatRequest.getMethod())) {
                commandHandler.doPerform(chatRequest, chatResponse);
                foundApplicableHandler = true;
                break;
            }
        }
        if (!foundApplicableHandler) {
            chatResponse.setStatusCode("405 Command Not Allowed");
        }

        ResponseService responseService = new ResponseService();
        String responseMessage = responseService.serializeAsString(chatResponse);

        return responseMessage;
    }
}
