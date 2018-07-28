package com.scholarcoder.chat.client.transport;

import com.scholarcoder.chat.client.store.session.Session;
import com.scholarcoder.chat.client.store.session.SessionStore;

import java.util.Arrays;
import java.util.Map;

import static com.scholarcoder.chat.client.transport.ServiceUtil.setHeadersAndBody;

public class RequestService {

    private SessionStore sessionStore;

    public RequestService() {
    }

    public RequestService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public ChatRequest deserializeRequestMessage(String requestMessage) {
        String[] requestLines = requestMessage.split("\n");

        ChatRequest chatRequest = new ChatRequest();
        setRequestLine(requestLines[0], chatRequest);
        setHeadersAndBody(chatRequest, requestLines);

        final Map<String, String> requestHeaders = chatRequest.getHeaders();
        if (requestHeaders != null) {
            injectSessionIfSessionHeaderExists(chatRequest, requestHeaders);
        }

        return chatRequest;
    }

    public String serializeAsString(ChatRequest chatRequest) {
        final Map<String, String> headers = chatRequest.getHeaders();
        final String body = chatRequest.getBody();

        StringBuilder responseBuilder = new StringBuilder();
        final boolean metaDataIsntEmpty = !chatRequest.getMetaData().isEmpty();
        responseBuilder.append(chatRequest.getMethod()).append(" ").append(metaDataIsntEmpty ? chatRequest.getMetaData() + " " : "").append("CHAT/1.0");

        if (!headers.isEmpty()) {
            responseBuilder.append("\n");
        }

        Integer headerCounter = 0;
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerCounter++;

            responseBuilder.append(header.getKey()).append(": ").append(header.getValue());
            if (headerCounter != headers.size()) {
                responseBuilder.append("\n");
            }
        }

        if (body != null && !body.isEmpty()) {
            responseBuilder.append("\n\n");
            responseBuilder.append(body);
        }

        return responseBuilder.toString();
    }

    private void injectSessionIfSessionHeaderExists(ChatRequest chatRequest, Map<String, String> requestHeaders) {
        if (requestHeaders.containsKey("SESSIONID")) {
            String sessionId = requestHeaders.get("SESSIONID");

            Session session = sessionStore.findBySessionId(sessionId);
            if (session != null) {
                chatRequest.setSession(session);
            }
        }
    }

    private void setRequestLine(String requestLine, ChatRequest chatRequest) {
        // TODO 14/07/18: Validate request line
        String[] requestLineTokens = requestLine.split(" ");
        // method is mandatory, meta is optional and protocol version is mandatory

        String method = requestLineTokens[0];
        String metaData = "";
        String protocolVersion = null;
        if (requestLineTokens.length == 2 && Arrays.asList(requestLineTokens).contains("CHAT/1.0")) {
            protocolVersion = requestLineTokens[1];
        }
        if (requestLineTokens.length == 3 && Arrays.asList(requestLineTokens).contains("CHAT/1.0")) {
            metaData = requestLineTokens[1];
            protocolVersion = requestLineTokens[2];
        }

        // TODO 14/07/18: Validate chat protocol version
        chatRequest.setRequestLine(method, metaData);
    }
}
