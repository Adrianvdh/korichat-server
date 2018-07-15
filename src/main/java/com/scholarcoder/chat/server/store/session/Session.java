package com.scholarcoder.chat.server.store.session;

import lombok.Data;

import java.util.UUID;

@Data
public class Session {
    private String sessionId;
    private String username;

    public Session(String username) {
        this.username = username;
        this.sessionId = UUID.randomUUID().toString();
    }
}
