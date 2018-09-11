package org.korichat.server.eventbus;

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

        Class sanitizedClass = sanitizeClassName(object.getClass());

        this.instanceObjects.put(sanitizedClass, object);
    }

    private Class sanitizeClassName(Class classToSanitize) {
        String sanitizedClassName = classToSanitize.getCanonicalName().split("\\$")[0];
        Class sanitizedClass = null;
        try {
            sanitizedClass = Class.forName(sanitizedClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sanitizedClass;
    }

    public void registerBean(Object bean) {
        Class sanitizedClass = sanitizeClassName(bean.getClass());

        this.beanObjects.put(sanitizedClass, bean);
    }

    public Map<Class, Object> getInstanceObjects() {
        return instanceObjects;
    }

    public Map<Class, Object> getBeanObjects() {
        return beanObjects;
    }
}
