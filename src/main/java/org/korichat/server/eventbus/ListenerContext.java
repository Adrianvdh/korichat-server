package org.korichat.server.eventbus;

import org.korichat.server.MessagePublisher;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ListenerContext {
    private Map<Class, Object> instanceObjects = new HashMap<>();
    private Map<Class, Object> beanObjects = new HashMap<>();

    public void registerListener(Object object) {
        // validate listener has at least one @EventHandler decorated method
        Class listenerClass = object.getClass();

        boolean hasAtleastOneEventHandlerMethod = false;
        for (Method method : listenerClass.getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                hasAtleastOneEventHandlerMethod = true;
                break;
            }
        }
        if(!hasAtleastOneEventHandlerMethod) {
            System.out.printf("Class %s doesn't have an @EventHandler method", listenerClass.getName());
            return;
        }

        this.instanceObjects.put(object.getClass(), object);
    }

    public void registerBean(Object bean) {
        this.beanObjects.put(bean.getClass(), bean);
    }

    public Map<Class, Object> getInstanceObjects() {
        return instanceObjects;
    }

    public Map<Class, Object> getBeanObjects() {
        return beanObjects;
    }
}
