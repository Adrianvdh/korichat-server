package com.scholarcoder.chat.server.store.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SessionStore {

    private Map<String, Session> sessions = new HashMap<>();

    public SessionStore() {
    }

    public void putSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    public Session findBySessionId(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        } else {
            throw new SessionNotFoundException("The session linked with id " + sessionId + " could not be found!");
        }
    }

    public Session findByUsername(String username) {
        return sessions.values().stream()
                .filter(session -> session.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new SessionNotFoundException("The session linked with username " + username + " could not be found!"));
    }

    public void clear() {
        sessions.clear();
    }

    public Collection<Session> findAll() {
        return sessions.values();
    }
}
