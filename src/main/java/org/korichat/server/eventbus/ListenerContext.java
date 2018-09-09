package org.korichat.server.eventbus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ListenerContext {
    private Map<Class, Object> instanceObjects = new HashMap<>();

    public void storeInstance(Object object) {
        // validate listener has at least one @EventHandler decorated method
        Class listenerClass = object.getClass();

        boolean hasAtleastOneEventHandlerMethod = false;
        for (Method method : listenerClass.getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                hasAtleastOneEventHandlerMethod = true;
            }
        }
        if(!hasAtleastOneEventHandlerMethod) {
            System.out.printf("Class %s doesn't have an @EventHandler method", listenerClass.getName());
            return;
        }

        this.instanceObjects.put(object.getClass(), object);
    }

    public Map<Class, Object> getInstanceObjects() {
        return instanceObjects;
    }
}
