package org.korichat.server;

import org.korichat.messaging.AckMessage;
import org.korichat.messaging.Message;
import org.korichat.messaging.protocol.FetchRequest;
import org.korichat.messaging.protocol.FetchResponse;
import org.korichat.messaging.protocol.Initialize;
import org.korichat.messaging.protocol.Subscribe;
import org.korichat.server.messagequeue.MessageQueue;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class MessagingHandlerStrategy implements HandlerStrategy<Message> {

    private ObjectOutputStream out;
    private MessageQueue messageQueue;
    private Set<String> subscribedTopics = new HashSet<>();

    public MessagingHandlerStrategy(ObjectOutputStream out, MessageQueue messageQueue) {
        this.out = out;
        this.messageQueue = messageQueue;
    }

    @Override
    public void handle(Message message) {
        MessagePublisher messagePublisher = new MessagePublisher(this.out);


        Class payloadType = message.getPayloadType();
        try {
            if (payloadType == Initialize.class) {
                ack(message);
            } else if (payloadType == Subscribe.class) {
                Message<Subscribe> subscribeMessage = message;
                String topic = subscribeMessage.getPayload().getTopic();

                // validate the authorization of the topic to subscribe to

                // add to the list of subscribed topic
                subscribedTopics.add(topic);
                ack(message);

            } else if (payloadType == FetchRequest.class) {
                Message<FetchRequest> fetchRequest = message;
                String topic = fetchRequest.getPayload().getTopic();
                Integer queuedMessagesOnTopic = messageQueue.countMessages(topic);

                reply(new FetchResponse(topic, queuedMessagesOnTopic), topic);

                for (int i = 0; i < queuedMessagesOnTopic; i++) {
                    Message oneMessage = messageQueue.take(topic);
                    reply(oneMessage);
                }
            } else {
                String topic = message.getTopic();
                messageQueue.createTopic(topic);

                messageQueue.putMessage(topic, message);

                ack(message);
            }
            // Reply with exception -> MessageException(No applicable message handler found!)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void reply(Message message) throws IOException {
        out.writeObject(message);
    }

    private <T> void reply(T payload, String topic) throws IOException {
        out.writeObject(new Message(payload, topic));
    }

    private void ack(Message message) throws IOException {
        AckMessage ackMessage = new AckMessage(message.getIdentifier());
        reply(ackMessage, message.getTopic());
    }
}
