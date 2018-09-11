package org.korichat.server.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
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
                    tryInvokeEventHandlerMethod(method, listenerInstance, event);
                }
            }
        }

    }

    private void tryInvokeEventHandlerMethod(Method method, Object listenerInstance, Object event) {
        Class[] parameterTypes = method.getParameterTypes();

        List<Object> argumentsToInvokeMethodWith = new LinkedList<>();

        // collect parameter
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = method.getParameterTypes()[i];

            // validate first parameter list
            if (i == 0 && parameterType != event.getClass()) {
                throw new RuntimeException("The first parameter has to be the an event");
            }

            // handle event argument
            if(i == 0) {
                argumentsToInvokeMethodWith.add(event);
                continue;
            }
            // add method argument type instance object
            if (listenerContext.getBeanObjects().containsKey(parameterType)) {
                argumentsToInvokeMethodWith.add(listenerContext.getBeanObjects().get(parameterType));
            }
        }

        Object[] argumentInstanceObjects = argumentsToInvokeMethodWith.toArray();

        // try invoke
        try {
            method.invoke(listenerInstance, argumentInstanceObjects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
