package org.korichat.server;

import org.korichat.messaging.AckMessage;
import org.korichat.messaging.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessagePublisher {

    private ObjectOutputStream out;

    public MessagePublisher(ObjectOutputStream out) {
        this.out = out;
    }

    public void publish(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private <T> void publish(T payload, String topic) {
        try {
            out.writeObject(new Message(payload, topic));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ack(Message message) {
        AckMessage ackMessage = new AckMessage(message.getIdentifier());
        publish(ackMessage, message.getTopic());
    }

}
