package com.scholarcoder.chat.client.transport;

public class InvalidChatRequestException extends RuntimeException {
    public InvalidChatRequestException(String message) {
        super(message);
    }
}
