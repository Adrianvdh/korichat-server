package org.korichat.server.eventbus.stubs;

import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.EventHandler;

public class TestMessageListener {

    @EventHandler
    public void handleMyEvent(MyEvent myEvent, MessagePublisher messagePublisher) {
        System.out.println("Invoked handleMyEvent(myEvent, publisher)");

        messagePublisher.publish(new Message<>("someId", "Reply", null, null));
    }


    @EventHandler
    public void handleMessageWithOtherDependencies(OtherEvent otherEvent, MessagePublisher messagePublisher, SomeService service) {
        System.out.println("Invoked handleMessageWithDependency(message, publisher, service)");

        messagePublisher.publish(new Message<>("someId", "Reply", null, null));
    }


    @EventHandler
    public void handleMessageString(String string, MessagePublisher messagePublisher) {
        System.out.println("Invoked handleMessageString");

        messagePublisher.publish(new Message<>("someId", "Reply", null, null));
    }
}
