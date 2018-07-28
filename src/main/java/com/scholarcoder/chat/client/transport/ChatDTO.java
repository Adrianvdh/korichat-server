package com.scholarcoder.chat.client.transport;

public interface ChatDTO {
    String getBody();

    void setBody(String body);

    void addHeader(String headerName, String headerValue);
}
