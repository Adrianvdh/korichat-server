package org.korichat.server.messagequeue;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Message<T> {
    private final String identifier;
    private T payload;
    private Map<String, String> headers;

    public Message(T payload) {
        this(UUID.randomUUID().toString(), payload, new HashMap<>());
    }

    public Message(T payload, Map<String, String> headers) {
        this(UUID.randomUUID().toString(), payload, headers);
    }

    public Message(String identifier, T payload, Map<String, String> headers) {
        this.identifier = identifier;
        this.payload = payload;
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if(this.headers != null) {
            this.headers.put(key, value);
        }
    }
}
