package org.korichat.server.messagequeue;

public class MessageQueueSingleton {
    private static MessageQueueSingleton instance = new MessageQueueSingleton();
    private MessageQueue messageQueue;

    private MessageQueueSingleton() {
    }

    public static MessageQueueSingleton getInstance() {
        return instance;
    }

    public MessageQueue getMessageQueue() {
        if (messageQueue == null) {
            messageQueue = new MessageQueue();
        }
        return messageQueue;
    }
}
