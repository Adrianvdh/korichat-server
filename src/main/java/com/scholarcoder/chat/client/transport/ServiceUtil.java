package com.scholarcoder.chat.client.transport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtil {

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
