package org.korichat.server.eventbus;

import org.junit.Test;
import org.korichat.messaging.Message;
import org.korichat.server.MessagePublisher;
import org.korichat.server.eventbus.stubs.ComponentScanner;
import org.korichat.server.eventbus.stubs.MyEvent;
import org.korichat.server.eventbus.stubs.TestMessageListener;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.List;

public class EventBusTest {

    @Test
    public void invokeMethod() {
        ObjectOutputStream mockObjectOutputStream = Mockito.mock(ObjectOutputStream.class);
        MessagePublisher publisher = new MessagePublisher(mockObjectOutputStream);

        TestMessageListener listener = new TestMessageListener();
        ListenerContext context = new ListenerContext();
        context.registerBean(publisher);
        context.registerListener(listener);


        MyEvent myEvent = new MyEvent("Hello");
        EventBus eventBus = new EventBus(context);


        eventBus.publish(myEvent);

//        Mockito.verify(publisher).publish(new Message("Response", "example.topic"));
    }

    @Test
    public void testThreadLocal() {
        ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return super.initialValue();
            }
        };

        new Thread(() -> {
            threadLocal.set("Thread1");

        }).start();

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
