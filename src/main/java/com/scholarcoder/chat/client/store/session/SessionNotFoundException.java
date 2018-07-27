package com.scholarcoder.chat.client.store.session;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String message) {
        super(message);
    }
}
