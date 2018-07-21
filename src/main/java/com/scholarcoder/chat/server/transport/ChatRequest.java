package com.scholarcoder.chat.server.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor
public class ChatRequest {
    private String method;
    private String metaData;
    private Map<String, String> headers;
    private String body;

    public ChatRequest() {
    }

    public void setRequestLine(String method, String metaData) {
        this.method = method;
        this.metaData = metaData;
    }

    public void putHeader(String headerName, String headeralue) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(headerName, headeralue);
    }

    public static ChatRequest fromRequest(String request) {
        String[] requestLines = request.split("\n");

        ChatRequest chatRequest = new ChatRequest();
        setChatRequest_RequestLine(requestLines[0], chatRequest);

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

    private static void setChatRequest_RequestLine(String requestLine, ChatRequest chatRequest) {
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

    private static void extractResponseHeader(ChatRequest chatRequest, String property) {
        String[] headerLine = parseHeader(property);
        String headerName = headerLine[0].trim();
        String headerValue = headerLine[1].trim();
        chatRequest.putHeader(headerName, headerValue);
    }

    private static boolean beforeBodySection(String property) {
        return "".equals(property);
    }


    private static String[] parseHeader(String headerLine) {
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

    private static String extractHeaderName(String headerLine) {
        return headerLine.split(":")[0];
    }
}
