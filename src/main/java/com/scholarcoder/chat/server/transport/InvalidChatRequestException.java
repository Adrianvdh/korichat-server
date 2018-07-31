package com.scholarcoder.chat.server.transport;

public class InvalidChatRequestException extends RuntimeException {
    public InvalidChatRequestException(String message) {
        super(message);
    }
}
