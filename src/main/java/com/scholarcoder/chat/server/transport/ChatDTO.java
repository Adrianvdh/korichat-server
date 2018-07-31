package com.scholarcoder.chat.server.transport;

public interface ChatDTO {
    String getBody();

    void setBody(String body);

    void addHeader(String headerName, String headerValue);
}
