package com.scholarcoder.chat.server.store.session;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;

public class SessionStore {
    private Map<Session, Future> connections = new WeakHashMap<>();

    public void putConnectionHandle(Session session, Future clientHandle) {
        connections.put(session, clientHandle);
    }

    public Future getClientHandle(Session session) {
        if (connections.containsKey(session)) {
            return connections.get(session);
        }
        return null;
    }

    public void hotswapSessionId(Session currentSession, Session newSession) {
        if (connections.containsKey(currentSession)) {
            Future connectionHandle = connections.get(currentSession);

            putConnectionHandle(newSession, connectionHandle);
        }
    }
}
