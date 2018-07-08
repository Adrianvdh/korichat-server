package com.scholarcoder.chat.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageProcessorTest {

    MessageProcessor messageProcessor;

    @Before
    public void setUp() throws Exception {
        messageProcessor = new MessageProcessor();
    }

    @Test
    public void testRegisterUser() {
        String command = "REG adrian";
        String expectedResponse = Responses.CREATED;

        String response = messageProcessor.process(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegisterUserThatAlreadyExists() {
        String command = "REG adrian";
        String expectedResponse = Responses.CONFLICT;

        messageProcessor.process(command);
        String response = messageProcessor.process(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testListRegisteredUsers() {
        messageProcessor.process("REG adrian");
        messageProcessor.process("REG josie");

        String command = "LISTUSER";
        String expectedResponse = "adrian,josie";

        String response = messageProcessor.process(command);

        Assert.assertEquals(expectedResponse, response);
    }
}