package org.korichat.server.eventbus;

import org.junit.Test;
import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.stubs.ComponentScanner;
import org.korichat.server.eventbus.stubs.TestEventObject;
import org.korichat.server.eventbus.stubs.TestMessageListener;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class EventBusTest {

    @Test
    public void testPublishMessage_HandlerAcknowledges() {
        TestMessageListener listener = new TestMessageListener();
        MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

        ListenerContext context = new ListenerContext();
        context.registerBean(publisher);
        context.registerListener(listener);

        EventBus eventBus = new EventBus(context);

        Message message = new Message("", null);
        eventBus.publish(message);

        Mockito.verify(publisher).ack(message);
    }

    @Test(expected = EventHandlerException.class)
    public void testPublishNonMessage_rejectsHandlerMethod_expectInvalidHandler() {
        TestMessageListener listener = new TestMessageListener();
        MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

        ListenerContext context = new ListenerContext();
        context.registerBean(publisher);
        context.registerListener(listener);

        EventBus eventBus = new EventBus(context);
        eventBus.publish(new TestEventObject());
    }

    @Test
    public void testPublishMessage_usingComponentScan() throws URISyntaxException, IOException, ClassNotFoundException {
        ComponentScanner componentScanner = new ComponentScanner();
        List<Class> classes = componentScanner.getClasses("org.korichat.server.api");

        for (Class aClass : classes) {
            System.out.println("aClass = " + aClass.getName());
        }


    }
}
