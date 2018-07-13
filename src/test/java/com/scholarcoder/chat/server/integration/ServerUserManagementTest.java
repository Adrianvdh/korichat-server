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

        System.out.println("Sending command");
        String response = client.sendCommand(command);
        System.out.println("Finished sending command");

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegisterUserThatAlreadyExists() {
        String command = "REG adrian CHAT/1.0";
        String expectedResponse = "409 Conflict";
        Client client = new Client(HOST, PORT);


        System.out.println("Sending command");
        client.sendCommand(command);
        String response = client.sendCommand(command);
        System.out.println("Finished sending command");

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testListRegisteredUsers() {
        Client client = new Client(HOST, PORT);
        client.sendCommand("REG adrian CHAT/1.0");
        client.sendCommand("REG adrian CHAT/1.0");

        String command = "LISTUSER";
        String expectedResponse = "adrian,josie";

        System.out.println("Sending command");
        String response = client.sendCommand(command);
        System.out.println("Finished sending command");

        Assert.assertEquals(expectedResponse, response);
    }

}
