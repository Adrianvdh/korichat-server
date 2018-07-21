package com.scholarcoder.chat.server.integration;

import com.scholarcoder.chat.server.Client;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ServerUserManagementTest extends AbstractServerTest {

    @Test
    public void testRegisterUser() {
        Client client = new Client(HOST, PORT);
        String expectedResponse = "CHAT/1.0 201 Created";

        String command = "REG adrian CHAT/1.0";
        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegisterUserThatAlreadyExists() {
        String command = "REG adrian CHAT/1.0";
        String expectedResponse = "CHAT/1.0 409 Conflict";
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

        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "\n"
                + "adrian,josie";

        String response = client.sendCommand("LISTUSER CHAT/1.0");

        Assert.assertEquals(expectedResponse, response);

    }

    @Ignore
    @Test
    public void testUseUser_expectSessionResponseLooksLikeAppropriateResponse() {
        Client client = new Client(HOST, PORT);
        client.sendCommand("REG adrian CHAT/1.0");

        String command = "CHAT/1.0 USE adrian";

        String expectedResponseRegex = "CHAT/1.0 200 OK\n"
                + "Set-Cookie: SESSIONID=" + uuidRegexMatcher();

        String actualResponse = client.sendCommand(command);

        Assert.assertTrue(actualResponse.matches(expectedResponseRegex));
    }

    private String uuidRegexMatcher() {
        return "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
    }

}
