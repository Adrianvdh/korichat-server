package com.scholarcoder.chat.server.transport;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestService {

    public ChatRequest parseRequestMessage(String requestMessage) {
        String[] requestLines = requestMessage.split("\n");

        ChatRequest chatRequest = new ChatRequest();
        setRequestLine(requestLines[0], chatRequest);

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
        return chatRequest;
    }

    private boolean beforeBodySection(String property) {
        return "".equals(property);
    }

    private void setRequestLine(String requestLine, ChatRequest chatRequest) {
        // TODO 14/07/18: Validate request line
        String[] requestLineTokens = requestLine.split(" ");
        // method is mandatory, meta is optional and protocol version is mandatory

        String method = requestLineTokens[0];
        String metaData = null;
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
