package com.scholarcoder.chat.server.transport;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseService {

    public String serializeAsString(ChatResponse chatResponse) {
        final Map<String, String> headers = chatResponse.getHeaders();
        final String body = chatResponse.getBody();

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("CHAT/1.0 ").append(chatResponse.getStatusCode());

        if (!headers.isEmpty()) {
            responseBuilder.append("\n");
        }
        AtomicInteger headerCounter = new AtomicInteger();
        headers.forEach((key, value) -> {
            headerCounter.getAndIncrement();
            responseBuilder.append(key).append(": ").append(value);
            if(headerCounter.get() != headers.size()) {
                responseBuilder.append("\n");
            }
        });

        if (body != null && !body.isEmpty()) {
            responseBuilder.append("\n\n");
            responseBuilder.append(body);
        }

        return responseBuilder.toString();
    }
}
