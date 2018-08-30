package org.korichat.server.processor;

import org.korichat.server.api.CommandHandler;
import org.korichat.server.store.session.SessionStoreSingelton;
import org.korichat.server.protocol.ChatRequest;
import org.korichat.server.protocol.ChatResponse;
import org.korichat.server.protocol.RequestService;
import org.korichat.server.protocol.ResponseService;

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
                commandHandler.handle(chatRequest, chatResponse);
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
