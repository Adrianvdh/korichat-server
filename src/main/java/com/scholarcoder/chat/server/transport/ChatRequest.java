package com.scholarcoder.chat.server.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor
public class ChatRequest {
    private RequestLine requestLine;
    private Map<String, String> headers;
    private String body;

    public ChatRequest() {
    }

    public void setRequestLine(String command, String meta, String protocolVersion) {
        this.requestLine = RequestLine.builder()
                .command(command)
                .metadata(meta)
                .protocolVersion(protocolVersion).build();
    }

    public void putHeader(String key, String value) {
        if(headers == null) {
            headers = new HashMap<>();
        }

        headers.put(key, value);
    }

    public static ChatRequest fromRequest(String request) {
        String[] requestLines = request.split("\n");

        ChatRequest chatRequest = new ChatRequest();

        String[] requestLine = requestLines[0].split(" ");
        chatRequest.setRequestLine(requestLine[0], requestLine[1], requestLine[2]);

        boolean atBody = false;
        for (String property : requestLines) {
            if(property == requestLines[0]) {
                continue;
            }
            if("".equals(property)) {
                atBody = true;
                continue;
            }
            if(atBody) {
                chatRequest.setBody(property);
            }
            else {
                String[] headerLine = splitHeaderField(property);
                chatRequest.putHeader(headerLine[0].trim(), headerLine[1].trim());
            }
        }
        return chatRequest;
    }


    static String[] splitHeaderField(String header) {
        String headerProperty = header.split(":")[0];
        final String regex = "(?<=\\w:).*";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(header);

        String headereValue = null;
        while (matcher.find()) {
            headereValue = matcher.group(0);
        }
        return new String[]{headerProperty, headereValue};
    }

    @Builder
    @Data
    @AllArgsConstructor
    private static class RequestLine {
        private String command;
        private String metadata;
        private String protocolVersion;
    }
}
