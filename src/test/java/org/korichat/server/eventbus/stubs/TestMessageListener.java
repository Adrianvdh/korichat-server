package org.korichat.server.eventbus.stubs;

import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.EventHandler;

public class TestMessageListener {

    @EventHandler
    public void handleMyEvent(MyEvent myEvent, MessagePublisher messagePublisher) {
        System.out.println("handleMyEvent :: invoked!");
//        messagePublisher.publish(new Message("Response", "example.topic"));

    }

//    @EventHandler
//    public void handleMyEvent(MyEvent myEvent, MessagePublisher messagePublisher, SessionContext sessionContext) {
//        System.out.println("TestMessageListener :: handleMyEvent");
//    }


}
