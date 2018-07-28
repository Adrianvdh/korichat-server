package com.scholarcoder.chat.client.transport;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtil {

    // Serialization utils
    public static void appendStatusCode(StringBuilder responseBuilder, String s, String statusCode) {
        responseBuilder.append(s).append(statusCode);
    }

    public static void appendHeaders(Map<String, String> headers, StringBuilder responseBuilder) {
        Integer headerCounter = 0;
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerCounter++;

            appendStatusCode(responseBuilder.append(header.getKey()), ": ", header.getValue());
            if (headerCounter != headers.size()) {
                responseBuilder.append("\n");
            }
        }
    }

    public static void appendBody(String body, StringBuilder responseBuilder) {
        if (body != null && !body.isEmpty()) {
            responseBuilder.append("\n\n");
            responseBuilder.append(body);
        }
    }


    // Deserialization utils
    public static void setHeadersAndBody(ChatDTO chatDTO, String[] responseLines) {
        boolean atBody = false;
        for (String property : responseLines) {
            if (property == responseLines[0]) {
                continue;
            }
            if (beforeBodySection(property)) {
                atBody = true;
                continue;
            }
            if (atBody) {
                chatDTO.setBody(property);
            } else {
                extractResponseHeader(chatDTO, property);
            }
        }
    }

    public static boolean beforeBodySection(String property) {
        return "".equals(property);
    }

    public static void extractResponseHeader(ChatDTO chatDTO, String property) {
        String[] headerLine = parseHeader(property);
        String headerName = headerLine[0].trim();
        String headerValue = headerLine[1].trim();
        chatDTO.addHeader(headerName, headerValue);
    }

    public static String[] parseHeader(String headerLine) {
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

    public static String extractHeaderName(String headerLine) {
        return headerLine.split(":")[0];
    }
}
