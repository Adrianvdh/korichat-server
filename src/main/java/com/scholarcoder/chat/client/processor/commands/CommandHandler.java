package com.scholarcoder.chat.client.processor.commands;

import com.scholarcoder.chat.client.transport.ChatRequest;
import com.scholarcoder.chat.client.transport.ChatResponse;

public interface CommandHandler {
    boolean applicable(String action);

    void doPerform(ChatRequest request, ChatResponse response);
}
