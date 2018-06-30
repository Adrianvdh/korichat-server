package com.pingpongchat.server;

public interface ClientHandler {
    void handle();

    void sendMessage(String message);
}
