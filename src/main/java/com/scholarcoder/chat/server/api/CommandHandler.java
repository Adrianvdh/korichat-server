package com.scholarcoder.chat.server.api;

import com.scholarcoder.chat.server.transport.ChatRequest;
import com.scholarcoder.chat.server.transport.ChatResponse;

public interface CommandHandler {
    boolean applicable(String action);

    void doPerform(ChatRequest request, ChatResponse response);
}