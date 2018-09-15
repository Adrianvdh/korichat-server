package org.korichat.server.eventbus;

public class EventBusException extends RuntimeException{
    public EventBusException(String message) {
        super(message);
    }
}
