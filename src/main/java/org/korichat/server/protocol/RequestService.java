package org.korichat.server.protocol;

import org.korichat.server.store.session.Session;
import org.korichat.server.store.session.SessionStore;

import java.util.Arrays;
import java.util.Map;

public class RequestService {

    private SessionStore sessionStore;

    public RequestService() {
        sessionStore = new SessionStore();
    }

    public RequestService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public String serializeAsString(ChatRequest chatRequest) {
        final Map<String, String> headers = chatRequest.getHeaders();
        final String body = chatRequest.getBody();

        StringBuilder responseBuilder = new StringBuilder();
        appendRequestLine(chatRequest, responseBuilder);

        if (!headers.isEmpty()) {
            responseBuilder.append("\n");
        }

        ServiceUtil.appendHeaders(headers, responseBuilder);
        ServiceUtil.appendBody(body, responseBuilder);

        return responseBuilder.toString();
    }

    private void appendRequestLine(ChatRequest chatRequest, StringBuilder responseBuilder) {
        final boolean metaDataIsntEmpty = !chatRequest.getMetaData().isEmpty();
        responseBuilder.append(chatRequest.getMethod()).append(" ").append(metaDataIsntEmpty ? chatRequest.getMetaData() + " " : "").append("CHAT/1.0");
    }

    public ChatRequest deserializeRequestMessage(String requestMessage) {
        String[] requestLines = requestMessage.split("\n");

        ChatRequest chatRequest = new ChatRequest();
        setRequestLine(requestLines[0], chatRequest);
        ServiceUtil.setHeadersAndBody(chatRequest, requestLines);

        final Map<String, String> requestHeaders = chatRequest.getHeaders();
        if (requestHeaders != null) {
            injectSessionIfSessionHeaderExists(chatRequest, requestHeaders);
        }

        return chatRequest;
    }

    private void injectSessionIfSessionHeaderExists(ChatRequest chatRequest, Map<String, String> requestHeaders) {
        String sessionId = chatRequest.getCookie("SESSIONID");
        if (sessionId == null || sessionId.isEmpty()) {
            return;
        }

        Session session = sessionStore.findBySessionId(sessionId);
        if (session != null) {
            chatRequest.setSession(session);
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
