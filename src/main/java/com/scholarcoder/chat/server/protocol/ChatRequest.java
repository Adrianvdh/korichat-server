package com.scholarcoder.chat.server.protocol;

import com.scholarcoder.chat.server.store.session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class ChatRequest implements ChatDTO {
    private String method;
    private String metaData;
    private Map<String, String> headers;
    private String body;

    private Session session;

    public ChatRequest() {
    }

    public void setRequestLine(String method, String metaData) {
        this.method = method;
        this.metaData = metaData;
    }

    @Override
    public void addHeader(String headerName, String headerValue) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(headerName, headerValue);
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    public String getCookie(String name) {
        Map<String, String> headerCookies = headers.entrySet().stream()
                .filter(entry -> entry.getKey().equals("Cookie"))
                .map(entry -> entry.getValue().split("="))
                .collect(Collectors.toMap(key -> key[0], value -> value[1]));

        return headerCookies.entrySet().stream()
                .filter(entry -> entry.getKey().equals(name))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);


    }
}
