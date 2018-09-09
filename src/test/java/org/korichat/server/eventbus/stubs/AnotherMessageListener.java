package org.korichat.server.eventbus.stubs;

import org.korichat.messaging.Message;
import org.korichat.messaging.protocol.Subscribe;
import org.korichat.server.eventbus.EventHandler;

public class AnotherMessageListener {

    @EventHandler
    public void handleMyEvent(MyEvent myEvent) {
        System.out.println("AnotherMessageListener :: handleMyEvent: " + myEvent.toString());
    }

    @EventHandler
    public void handleSubscribe(Message<Subscribe> myEvent) {
        System.out.println("AnotherMessageListener :: handleSubscribe: " + myEvent.toString());
    }
}
