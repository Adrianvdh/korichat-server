package com.scholarcoder.chat.server.transport;

import com.scholarcoder.chat.server.store.session.Session;
import com.scholarcoder.chat.server.store.session.SessionStore;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class RequestService {

    private SessionStore sessionStore;

    public RequestService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public ChatRequest parseRequestMessage(String requestMessage) {
        String[] requestLines = requestMessage.split("\n");

        ChatRequest chatRequest = new ChatRequest();
        setRequestLine(requestLines[0], chatRequest);
        setHeadersAndBody(requestLines, chatRequest);

        final Map<String, String> requestHeaders = chatRequest.getHeaders();
        if(requestHeaders != null) {
            injectSessionIfSessionHeaderExists(chatRequest, requestHeaders);
        }

        return chatRequest;
    }

    private void injectSessionIfSessionHeaderExists(ChatRequest chatRequest, Map<String, String> requestHeaders) {
        if(requestHeaders.containsKey("SESSIONID")) {
            String sessionId = requestHeaders.get("SESSIONID");

            Session session = sessionStore.findBySessionId(sessionId);
            if(session != null) {
                chatRequest.setSession(session);
            }
        }
    }

    private void setHeadersAndBody(String[] requestLines, ChatRequest chatRequest) {
        boolean atBody = false;
        for (String property : requestLines) {
            if (property == requestLines[0]) {
                continue;
            }
            if (beforeBodySection(property)) {
                atBody = true;
                continue;
            }
            if (atBody) {
                chatRequest.setBody(property);
            } else {
                extractResponseHeader(chatRequest, property);
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

    private boolean beforeBodySection(String property) {
        return "".equals(property);
    }

    private void extractResponseHeader(ChatRequest chatRequest, String property) {
        String[] headerLine = parseHeader(property);
        String headerName = headerLine[0].trim();
        String headerValue = headerLine[1].trim();
        chatRequest.putHeader(headerName, headerValue);
    }

    private String[] parseHeader(String headerLine) {
        String headerName = extractHeaderName(headerLine);
        final String headerValueSelectorRegex = "(?<=\\w:).*";

        Pattern pattern = Pattern.compile(headerValueSelectorRegex);
        Matcher matcher = pattern.matcher(headerLine);

        String headerValue = null;
        while (matcher.find()) {
            headerValue = matcher.group(0);
        }
        return new String[]{headerName, headerValue};
    }

    private String extractHeaderName(String headerLine) {
        return headerLine.split(":")[0];
    }
}
