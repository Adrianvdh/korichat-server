package org.korichat.server.eventbus;

public class EventHandlerException extends RuntimeException {
    public EventHandlerException(String message) {
        super(message);
    }
}
