package com.scholarcoder.chat.client.processor.commands.exceptions;

public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
