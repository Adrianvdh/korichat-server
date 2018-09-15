package org.korichat.server.eventbus;

import org.junit.Test;
import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.stubs.MyEvent;
import org.korichat.server.eventbus.stubs.OtherEvent;
import org.korichat.server.eventbus.stubs.TestMessageListener;
import org.mockito.Mockito;

public class EventBusTest {

    @Test
    public void testPublishArbitraryObjectAsEvent_expectHandlerToAccept() {
        TestMessageListener listener = new TestMessageListener();
        MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

        ListenerContext context = new ListenerContext();
        context.registerListener(listener);
        context.registerBean(publisher);

        EventBus eventBus = new EventBus(context);

        MyEvent myEvent = new MyEvent("Hello");
        eventBus.publish(myEvent);

        Mockito.verify(publisher).publish(new Message<>("someId", "Reply", null, null));
    }

    @Test(expected = EventHandlerException.class)
    public void testPublishMessage_hasApplicableHandler_noBeanObjectsToInjectForRequiredArguments_expectException() {
        TestMessageListener listener = new TestMessageListener();
        MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

        ListenerContext context = new ListenerContext();
        context.registerListener(listener);
        context.registerBean(publisher);
        // SomeService is not registered here

        EventBus eventBus = new EventBus(context);

        OtherEvent otherEvent = new OtherEvent();
        eventBus.publish(otherEvent);
    }


    @Test(expected = EventBusException.class)
    public void testPublishString_expectNoInteractions() {
        EventBus eventBus = new EventBus(new ListenerContext());

        eventBus.publish("some string");
    }

}
