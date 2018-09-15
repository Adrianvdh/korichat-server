package org.korichat.server.eventbus;

import org.korichat.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventBus {
    private Logger logger = LoggerFactory.getLogger(EventBus.class);

    private ListenerContext listenerContext;

    public EventBus(ListenerContext listenerContext) {
        this.listenerContext = listenerContext;
    }

    public void publish(Object event) {

        Class[] illegalTypes = {Number.class, String.class, Character.class};

        if (Arrays.asList(illegalTypes).contains(event.getClass())) {
            throw new EventBusException("This type of even is not supported!!!");
        }


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
        Map<Class, Object> availableBeans = listenerContext.getBeanObjects();

        Class[] parameterTypes = method.getParameterTypes();
        List<Object> methodArgumentDependencies = new LinkedList<>();

        // resolve method argument dependencies
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];

            // validate first parameter is arbitrary type exclude primitives
            if (i == 0) {

                Class[] illegalTypes = {Number.class, String.class, Character.class};

                if (Arrays.asList(illegalTypes).contains(parameterType)) {
                    logger.warn("@EventHandler method {}() has an illegal first parameter type of {}!", method.getName(), parameterType.getName());
                    return;
                }

                // method is not applicable for event
                if(parameterType != event.getClass()) {
                    return;
                }

                // handle event argument
                methodArgumentDependencies.add(event);
                continue;
            }

            // add method argument type instance object
            if (availableBeans.containsKey(parameterType)) {
                methodArgumentDependencies.add(availableBeans.get(parameterType));
            }
            else {
                throw new EventHandlerException(String.format("@EventHandler method %s() requires a dependency '%s' that is not available", method.getName(), parameterType.getName()));
            }
        }

        Object[] argumentInstanceObjects = methodArgumentDependencies.toArray();

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
