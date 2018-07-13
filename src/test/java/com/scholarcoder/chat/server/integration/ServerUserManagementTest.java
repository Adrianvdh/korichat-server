package com.scholarcoder.chat.server.integration;

import com.scholarcoder.chat.server.Client;
import org.junit.Assert;
import org.junit.Test;

public class ServerUserManagementTest extends AbstractServerTest {

    @Test
    public void testRegisterUser() {
        String command = "REG adrian CHAT/1.0";
        String expectedResponse = "201 Created";
        Client client = new Client(HOST, PORT);

        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegisterUserThatAlreadyExists() {
        String command = "REG adrian CHAT/1.0";
        String expectedResponse = "409 Conflict";
        Client client = new Client(HOST, PORT);


        client.sendCommand(command);
        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testListRegisteredUsers() {
        Client client = new Client(HOST, PORT);
        client.sendCommand("REG adrian CHAT/1.0");
        client.sendCommand("REG josie CHAT/1.0");

        String command = "LISTUSER";
        String expectedResponse = "adrian,josie";

        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }

}
