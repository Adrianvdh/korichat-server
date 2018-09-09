package org.korichat.server.eventbus.stubs;

public class MyEvent {
    private String message;

    public MyEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
