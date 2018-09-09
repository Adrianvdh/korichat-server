package org.korichat.server.eventbus;

import org.junit.Test;
import org.korichat.server.eventbus.stubs.ComponentScanner;
import org.korichat.server.eventbus.stubs.MyEvent;
import org.korichat.server.eventbus.stubs.TestMessageListener;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class EventBusTest {

    @Test
    public void invokeMethod() {
        TestMessageListener spy = Mockito.spy(new TestMessageListener());

        MyEvent myEvent = new MyEvent("Hello");
        spy.handleMyEvent(myEvent);

        Mockito.verify(spy, Mockito.times(1)).handleMyEvent(myEvent);
    }

    @Test
    public void testPublishMessage_usingInstanceContext() {
        TestMessageListener spy = Mockito.spy(new TestMessageListener());

        ListenerContext listenerContext = new ListenerContext();
        listenerContext.storeInstance(spy);

        EventBus eventBus = new EventBus(listenerContext);

        MyEvent myEvent = new MyEvent("Hello");
        eventBus.publish(myEvent);

        Mockito.verify(spy, Mockito.times(1)).handleMyEvent(myEvent);
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
