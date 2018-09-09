package org.korichat.server;

import com.scholarcoder.chat.client.AsyncClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korichat.messaging.AckMessage;
import org.korichat.messaging.Callback;
import org.korichat.messaging.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        List<Message<String>> sendMessages = new ArrayList<>();
        sendMessages.add(new Message<>("A", "message.user.adrian"));
        sendMessages.add(new Message<>("B", "message.user.adrian"));
        sendMessages.add(new Message<>("C", "message.user.adrian"));
        sendMessages.add(new Message<>("D", "message.user.adrian"));

        for (Message<String> message : sendMessages) {
            client.send(message).get();
        }

        client.subscribe("message.user.adrian");
        List<Message> polledMessages = client.poll(1000);

        Assert.assertEquals(sendMessages, polledMessages);

        client.disconnect();
    }
}
