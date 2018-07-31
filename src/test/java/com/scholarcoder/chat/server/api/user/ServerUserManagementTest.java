package com.scholarcoder.chat.server.api.user;

import com.scholarcoder.chat.client.Client;
import com.scholarcoder.chat.server.Server;
import org.junit.*;

public class ServerUserManagementTest {

    private static String HOST = "localhost";
    private static int PORT = 34567;

    private Server server;
    private Client client;

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.start();

        client = new Client(HOST, PORT);
        client.connect();

        UserRepositorySingleton.get().deleteAll();
    }

    @After
    public void tearDown() {
        client.disconnect();
        server.stop();
    }

    @Test
    public void testNotAllowedMethod() {
        String expectedResponse = "CHAT/1.0 405 Command Not Allowed";

        String command = "NOTEXISTANT sometmeta CHAT/1.0";
        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);

    }

    @Test
    public void testRegisterUser() {
        String expectedResponse = "CHAT/1.0 201 Created";

        String command = "REG adrian CHAT/1.0";
        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegisterUserThatAlreadyExists() {
        String command = "REG adrian CHAT/1.0";
        String expectedResponse = "CHAT/1.0 409 Conflict";

        client.sendCommand(command);
        String response = client.sendCommand(command);

        Assert.assertEquals(expectedResponse, response);
    }


    @Test
    public void testUseUser_expectSessionResponseLooksLikeAppropriateResponse() {
        client.sendCommand("REG adrian CHAT/1.0");

        String command = "USE adrian CHAT/1.0";

        String expectedResponseRegex = "CHAT/1.0 200 OK\n"
                + "Set-Cookie: SESSIONID=" + uuidRegexMatcher();

        String actualResponse = client.sendCommand(command);

        System.out.println("Server response:");
        System.out.println(actualResponse);

        Assert.assertTrue(actualResponse.matches(expectedResponseRegex));
    }

    private String uuidRegexMatcher() {
        return "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
    }

    @Test
    public void testListRegisteredUsers_requiresAuthentication_failAsNotLoggedIn() {
        client.sendCommand("REG adrian CHAT/1.0");
        client.sendCommand("REG josie CHAT/1.0");

        String expectedResponse = "CHAT/1.0 401 Unauthorized";

        String response = client.sendCommand("LISTUSER CHAT/1.0");

        Assert.assertEquals(expectedResponse, response);

    }


    @Ignore
    @Test
    public void testListRegisteredUsers_requiresAuthentication_succeeds() {
        client.sendCommand("REG adrian CHAT/1.0");

        String responseWithSessionId = client.sendCommand("USE adrian CHAT/1.0");
        System.out.println(responseWithSessionId);


        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "\n"
                + "adrian,josie";

        String response = client.sendCommand("LISTUSER CHAT/1.0");

        Assert.assertEquals(expectedResponse, response);

    }



}
