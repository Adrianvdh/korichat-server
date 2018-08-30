package com.scholarcoder.chat.server.processor;

import com.scholarcoder.chat.server.api.CommandHandler;
import com.scholarcoder.chat.server.store.session.SessionStoreSingelton;
import com.scholarcoder.chat.server.protocol.ChatRequest;
import com.scholarcoder.chat.server.protocol.ChatResponse;
import com.scholarcoder.chat.server.protocol.RequestService;
import com.scholarcoder.chat.server.protocol.ResponseService;

import java.util.List;

public class MessageProcessor {
    private List<CommandHandler> commandHandlers;

    public MessageProcessor() {
        this.commandHandlers = CommandHandlersRegistry.getInstance().getRegisteredCommandHandlers();
    }

    public String process(String message) {
        RequestService requestService = new RequestService(SessionStoreSingelton.get());
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
