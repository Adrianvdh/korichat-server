package org.korichat.server.api;

import org.korichat.server.protocol.ChatRequest;
import org.korichat.server.protocol.ChatResponse;

public interface CommandHandler {
    boolean applicable(String action);

    void handle(ChatRequest request, ChatResponse response);
}
