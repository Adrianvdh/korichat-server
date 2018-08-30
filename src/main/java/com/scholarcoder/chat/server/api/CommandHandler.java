package com.scholarcoder.chat.server.api;

import com.scholarcoder.chat.server.protocol.ChatRequest;
import com.scholarcoder.chat.server.protocol.ChatResponse;

public interface CommandHandler {
    boolean applicable(String action);

    void doPerform(ChatRequest request, ChatResponse response);
}
