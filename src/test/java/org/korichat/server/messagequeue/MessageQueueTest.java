package org.korichat.server.messagequeue;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class MessageQueueTest {

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

        // produce numbers onto message queue starting at 2 and ending at 20
        Thread producer = new Thread(() -> {
            for (int i = 0; i < numbersInOrder.length; i++) {
                int number = numbersInOrder[i];

                Message<Integer> message = new Message<>(number);
                messageQueue.putMessage("my.topic", message);
            }
        });
        producer.start();

        // receive messages off queue inorder starting at 2 and ending at 20
        for (int i = 0; i < numbersInOrder.length; i++) {
            Message<Integer> message = messageQueue.take("my.topic");
            int arrayNumber = numbersInOrder[i];
            int payloadNumber = message.getPayload();

            Assert.assertEquals(arrayNumber, payloadNumber);
        }
    }

    @Test
    @Ignore
    public void testQueue() {
        Queue<String> queue = new LinkedList<>();
        queue.add("abc");
        queue.add("def");
        queue.add("ghi");

        Assert.assertEquals("abc", queue.poll());
        Assert.assertEquals("def", queue.poll());
        Assert.assertEquals("ghi", queue.poll());

        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    @Ignore
    public void testBlockingQueue() {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        Thread producer = new Thread(() -> {
            while (true) {
                try {
                    int random = ThreadLocalRandom.current().nextInt(100);
                    System.out.println(Thread.currentThread().getName() + " gened: " + random);
                    queue.put(random);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        producer.start();

        System.out.println("Middle");

        while (true) {
            try {
                System.out.println("In consumer");
                Integer number = queue.take();
                System.out.println(Thread.currentThread().getName() + " result: " + number);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
