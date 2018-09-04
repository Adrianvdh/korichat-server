package org.korichat.server.messagequeue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    private Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private ConcurrentMap<String, BlockingQueue<Message>> topicsQueue;

    public MessageQueue() {
        topicsQueue = new ConcurrentHashMap<>();
    }

    public void createTopic(String topicName) {
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

        if(!topicsQueue.containsKey(topicName)) {
            topicsQueue.put(topicName, queue);
        }
    }

    public void putMessage(String topicName, Message message) {
        if(topicsQueue.containsKey(topicName)) {
            BlockingQueue<Message> topicQueue = topicsQueue.get(topicName);
            try {
                topicQueue.put(message);
            } catch (InterruptedException e) {
                logger.error("An error occurred while publishing a message onto the topic '{}'", topicName, e);
            }
        }
    }

    public <T> Message<T> take(String topicName) {
        if(topicsQueue.containsKey(topicName)) {
            BlockingQueue<Message> topicQueue = topicsQueue.get(topicName);
            try {
                return (Message<T>) topicQueue.take();
            } catch (InterruptedException e) {
                logger.error("An error occurred while taking a message off the topic '{}'", topicName, e);
            }
        }
        return null;
    }
}
