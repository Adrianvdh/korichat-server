package org.korichat.server;

import com.scholarcoder.chat.client.AsyncClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korichat.messaging.AckMessage;
import org.korichat.messaging.Callback;
import org.korichat.messaging.Message;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class AsyncClientTest {
    private static String HOST = "localhost";
    private static int PORT = 34567;

    private Server server;

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.start();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testConnectDisconnect() {
        AsyncClient client = new AsyncClient(HOST, PORT);
        client.connect();
        client.disconnect();
    }

    @Test
    public void testOnConnect_clientConnects() {
        AsyncClient client = new AsyncClient(HOST, PORT);

        client.onConnect((ack) -> {
            System.out.println("Acknowledged onConnect : InitId: " + ack.getIdentifier());
        });

        client.connect();
        client.disconnect();
    }

    @Test
    public void testSendMessage() throws ExecutionException, InterruptedException {
        AsyncClient client = new AsyncClient(HOST, PORT);
        client.connect();

        Message<String> sendMessage = new Message<>("Hello", "some.topic");

        Future<AckMessage> ackMessage = client.send(sendMessage);

        Assert.assertEquals(sendMessage.getIdentifier(), ackMessage.get().getIdentifier());
        client.disconnect();
    }

    @Test
    public void testSendWithCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AsyncClient client = new AsyncClient(HOST, PORT);
        client.connect();

        Message<String> sendMessage = new Message<>("Hello", "some.topic");

        client.send(sendMessage, new Callback() {
            @Override
            public void onAck(AckMessage ackMessage) {
                Assert.assertEquals(sendMessage.getIdentifier(), ackMessage.getIdentifier());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) { }
        });

        latch.await();
        client.disconnect();
    }

    @Test
    public void testReceiveOneMessage() throws ExecutionException, InterruptedException {
        AsyncClient client = new AsyncClient(HOST, PORT);
        client.connect();

        Message<String> message = new Message<>("Hello", "message.user.adrian");
        client.send(message).get();

        client.subscribe("message.user.adrian");
        List<Message> messages = client.poll(1000);

        Assert.assertEquals(messages.get(0), message);

        client.disconnect();
    }

    @Test
    public void testReceiveMultipleMessage() throws ExecutionException, InterruptedException {
        AsyncClient client = new AsyncClient(HOST, PORT);
        client.connect();

        Message<String> message = new Message<>("A", "message.user.adrian");
        Message<String> message2 = new Message<>("B", "message.user.adrian");
        Message<String> message3 = new Message<>("C", "message.user.adrian");
        Message<String> message4 = new Message<>("D", "message.user.adrian");
        client.send(message).get();
        client.send(message2).get();
        client.send(message3).get();
        client.send(message4).get();

        client.subscribe("message.user.adrian");
        List<Message> messages = client.poll(1000);

        Assert.assertEquals(messages.get(0), message);
        Assert.assertEquals(messages.get(1), message2);
        Assert.assertEquals(messages.get(2), message3);
        Assert.assertEquals(messages.get(3), message4);

        client.disconnect();
    }
}
