package com.scholarcoder.chat.server.integration;

import com.scholarcoder.chat.server.Client;
import org.junit.Assert;
import org.junit.Test;

public class ServerGroupManagementTest extends AbstractServerTest {

    @Test
    public void testCreateGroup() {
        String command = "MK_GROUP chat:adrian";
        String expectedResponse = "201 Created";
        Client client = new Client(HOST, PORT);

        System.out.println("Sending command");
        String response = client.sendCommand(command);
        System.out.println("Finished sending command");

        Assert.assertEquals(expectedResponse, response);
    }
}
