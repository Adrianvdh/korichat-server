package org.korichat.server.eventbus.stubs;

import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.EventHandler;

public class TestMessageListener {

    public void handleMessage() {

    }

    @EventHandler
    public void handleMessage(Message message, MessagePublisher messagePublisher) {
        messagePublisher.ack(message);

        System.out.println("Invoked here");
    }
}
