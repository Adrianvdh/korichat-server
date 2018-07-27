package com.scholarcoder.chat.client.processor;

import com.scholarcoder.chat.client.processor.commands.CommandHandler;
import com.scholarcoder.chat.client.processor.commands.CommandHandlersRegistry;
import com.scholarcoder.chat.client.transport.ChatRequest;
import com.scholarcoder.chat.client.transport.ChatResponse;
import com.scholarcoder.chat.client.transport.RequestService;
import com.scholarcoder.chat.client.transport.ResponseService;

import java.util.List;

public class MessageProcessor {
    private List<CommandHandler> commandHandlers;

    public MessageProcessor() {
        this.commandHandlers = CommandHandlersRegistry.getRegisteredCommandHandlers();
    }

    public String process(String message) {
        RequestService requestService = new RequestService();
        ChatRequest chatRequest = requestService.parseRequestMessage(message);

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
