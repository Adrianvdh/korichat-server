package com.scholarcoder.chat.client.transport;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scholarcoder.chat.client.transport.ServiceUtil.setHeadersAndBody;

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
            if (headerCounter.get() != headers.size()) {
                responseBuilder.append("\n");
            }
        });

        if (body != null && !body.isEmpty()) {
            responseBuilder.append("\n\n");
            responseBuilder.append(body);
        }

        return responseBuilder.toString();
    }

    public ChatResponse deserializeResponseMessage(String responseMessage) {
        String[] requestLines = responseMessage.split("\n");

        ChatResponse chatResponse = new ChatResponse();
        parseResponseCode(chatResponse, requestLines[0]);
        setHeadersAndBody(chatResponse, requestLines);
        return chatResponse;
    }

    private void parseResponseCode(ChatResponse chatResponse, String responseLine) {
        String[] responseLineTokens = responseLine.split(" ");
        // protocol version and status code are mandatory

        String protocolVersion = responseLineTokens[0];
        String statusCode = responseLineTokens[1] + " " + responseLineTokens[2];

        // TODO 14/07/18: Validate chat protocol version
        chatResponse.setStatusCode(statusCode);
    }

}
