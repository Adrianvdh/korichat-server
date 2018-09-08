package org.korichat.server;

public interface HandlerStrategy<T> {
    void handle(T request);
}
