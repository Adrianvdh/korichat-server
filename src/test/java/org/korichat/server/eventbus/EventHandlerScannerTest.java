package org.korichat.server.eventbus;

import org.junit.Assert;
import org.junit.Test;
import org.korichat.server.eventbus.stubs.TestMessageListener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class EventHandlerScannerTest {

    @Test
    public void testScanForEventHandlerMethods() {

        ComponentScanner componentScanner = new ComponentScanner();

        Map<Class, List<Method>> eventHandlerMethods = componentScanner.findEventHandlerClasses("org.korichat.server.eventbus.stubs");

        Assert.assertTrue(eventHandlerMethods.containsKey(TestMessageListener.class));
    }
}
