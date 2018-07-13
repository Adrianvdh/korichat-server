package com.scholarcoder.chat.server.integration;

import com.scholarcoder.chat.server.Client;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ServerGroupManagementTest extends AbstractServerTest {

    @Ignore
    @Test
    public void testCreateGroup() {
        String command = "MK_GROUP chat:adrian";
        String expectedResponse = "201 Created";
        Client client = new Client(HOST, PORT);

        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }
}
