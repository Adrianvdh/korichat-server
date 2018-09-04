package org.korichat.server.messagequeue;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class MessageQueueTest {

    private Logger logger = LoggerFactory.getLogger(MessageQueueTest.class);

    @Test
    public void testPutOneMessage() {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.createTopic("chat.api.user");

        Message<String> message = new Message<>("message payload");
        message.addHeader("userId", UUID.randomUUID().toString());

        messageQueue.putMessage("chat.api.user", message);

        Message takenMessage = messageQueue.take("chat.api.user");

        Assert.assertEquals(message, takenMessage);
    }

    @Test
    public void testProducerConsumerOnTopic_measureInOrder() {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.createTopic("my.topic");

        int[] numbersInOrder = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};

        // produce numbers onto the message queue starting at 2 and ending at 20
        Thread producer = new Thread(() -> {
            for (int i = 0; i < numbersInOrder.length; i++) {
                int number = numbersInOrder[i];

                Message<Integer> message = new Message<>(number);
                messageQueue.putMessage("my.topic", message);
            }
        });
        producer.start();

        // receive messages off the queue inorder starting at 2 and ending at 20
        for (int i = 0; i < numbersInOrder.length; i++) {
            Message<Integer> message = messageQueue.take("my.topic");
            assertPayloadIsInOrder(numbersInOrder, i, message);
        }
    }

    @Test
    public void testProducingAndConsumingFromMultipleTopicsConcurrently() throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue();
        String[]  topics = { "topic1", "topic2", "topic3"};
        for (String topic : topics) {
            logger.info("Creating topic {}", topic);
            messageQueue.createTopic(topic);
        }

        int[] numbersInOrder = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};

        // create a producer thread for each topic
        List<Thread> producerThreads = new ArrayList();
        for (String topic : topics) {
            String threadName = "p-thread:" + topic;
            Thread producerThread = createThread(threadName, () -> {
                for (int i = 0; i < numbersInOrder.length; i++) {

                    int number = numbersInOrder[i];
                    logger.info("{}: Publishing message {}", threadName, number);

                    Message<Integer> message = new Message<>(number);
                    messageQueue.putMessage(topic, message);
                }
            });
            producerThreads.add(producerThread);
        }
        producerThreads.forEach(Thread::start);

        CountDownLatch latch = new CountDownLatch(3);

        // create a consumer thread for each topic
        for (String topic : topics) {
            String threadName = "c-thread:" + topic;
            Thread consumerThread = new Thread(() -> {
                for (int i = 0; i < numbersInOrder.length; i++) {

                    Message<Integer> message = messageQueue.take(topic);
                    logger.info("{}: Consumed message {}", threadName, message.getPayload());
                    assertPayloadIsInOrder(numbersInOrder, i, message);
                }
                latch.countDown();

            }, threadName);
            consumerThread.start();
        }

        latch.await();
    }

    private Thread createThread(String threadName, Runnable runnable) {
        return new Thread(runnable, threadName);
    }

    private void assertPayloadIsInOrder(int[] numbersInOrder, int i, Message<Integer> message) {
        int arrayNumber = numbersInOrder[i];
        int payloadNumber = message.getPayload();

        Assert.assertEquals(arrayNumber, payloadNumber);
    }

    @Test
    public void testGetMessageQueueFromSingleton() {
        MessageQueue messageQueue = MessageQueueSingleton.getInstance().getMessageQueue();

        Assert.assertNotNull(messageQueue);
    }
}
