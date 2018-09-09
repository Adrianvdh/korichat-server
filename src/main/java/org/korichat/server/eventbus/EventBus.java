package org.korichat.server.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class EventBus {
    private ListenerContext listenerContext;

    public EventBus(ListenerContext listenerContext) {
        this.listenerContext = listenerContext;
    }

    public void publish(Object event) {
        Map<Class, Object> instanceObjects = listenerContext.getInstanceObjects();

        for (Map.Entry<Class, Object> instance : instanceObjects.entrySet()) {
            Class listenerClass = instance.getKey();
            Object listenerInstance = instance.getValue();

            for (Method method : listenerClass.getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        if(parameterType == event.getClass()) {
                            try {
                                method.invoke(listenerInstance, event);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }

    }
}
