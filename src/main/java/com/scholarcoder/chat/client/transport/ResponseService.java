package com.scholarcoder.chat.client.transport;

import java.util.Map;

import static com.scholarcoder.chat.client.transport.ServiceUtil.*;

public class ResponseService {

    public String serializeAsString(ChatResponse chatResponse) {
        final Map<String, String> headers = chatResponse.getHeaders();
        final String body = chatResponse.getBody();

        StringBuilder responseBuilder = new StringBuilder();
        appendStatusCode(responseBuilder, "CHAT/1.0 ", chatResponse.getStatusCode());

        if (!headers.isEmpty()) {
            responseBuilder.append("\n");
        }

        appendHeaders(headers, responseBuilder);
        appendBody(body, responseBuilder);

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
