package org.korichat.server.store.session;

import lombok.Data;

import java.util.UUID;

@Data
public class Session {
    private String sessionId;
    private String username;

    public Session() {
        sessionId = UUID.randomUUID().toString();
    }
}
