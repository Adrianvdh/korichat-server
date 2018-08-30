package com.scholarcoder.chat.server.protocol;

public class InvalidChatRequestException extends RuntimeException {
    public InvalidChatRequestException(String message) {
        super(message);
    }
}
