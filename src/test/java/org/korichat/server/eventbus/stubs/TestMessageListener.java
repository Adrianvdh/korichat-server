package org.korichat.server.eventbus.stubs;

import org.korichat.messaging.Message;
import org.korichat.server.eventbus.EventHandler;

public class TestMessageListener {

    @EventHandler
    public void handleMyEvent(MyEvent myEvent) {
        System.out.println("TestMessageListener :: handleMyEvent");
    }

    @EventHandler
    public void handleStringMessage(Message<String> message) {
        System.out.println("TestMessageListener :: handleStringMessage: " + message.toString());
    }

}
