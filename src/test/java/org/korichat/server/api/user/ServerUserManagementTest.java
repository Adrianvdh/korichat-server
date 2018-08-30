package org.korichat.server.api.user;

import com.scholarcoder.chat.client.Client;
import org.korichat.server.Server;
import org.korichat.server.api.user.repository.UserRepository;
import org.korichat.server.api.user.repository.UserRepositorySingleton;
import org.korichat.server.repository.EmbeddedDatabaseBuilder;
import org.korichat.server.repository.HsqldbConnection;
import org.korichat.server.protocol.ChatResponse;
import org.korichat.server.protocol.ResponseService;
import org.junit.*;

import java.util.Map;
import java.util.stream.Collectors;

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

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.configureConnection(HsqldbConnection.getInstance().getInprocessConnection());
        builder.addUpdateScript("hsqldb/create-schema.sql");
        builder.build();
    }

    @After
    public void tearDown() {
        client.disconnect();
        server.stop();

        UserRepository userRepository = UserRepositorySingleton.getInstance().get();
        userRepository.deleteAll();
    }

    @Test
    public void testNotAllowedMethod() {
        String expectedResponse = "CHAT/1.0 405 Command Not Allowed";

        String command = "NOTEXISTANT somemeta CHAT/1.0";
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

        String response = client.sendCommand("LIST_USER CHAT/1.0");

        Assert.assertEquals(expectedResponse, response);

    }


    @Test
    public void testListRegisteredUsers_requiresAuthentication_succeeds() {
        client.sendCommand("REG adrian CHAT/1.0");
        client.sendCommand("REG josie CHAT/1.0");

        ResponseService responseService = new ResponseService();
        String useUserResponse = client.sendCommand("USE adrian CHAT/1.0");
        ChatResponse chatResponse = responseService.deserializeResponseMessage(useUserResponse);

        Map<String, String> headerCookies = extractResponseSetCookieHeaders(chatResponse);

        String sessionId = headerCookies.get("SESSIONID");

        String requestMessage = "LIST_USER CHAT/1.0\n" +
                "Cookie: SESSIONID=" + sessionId;
        String listUserResponse = client.sendCommand(requestMessage);

        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "\n"
                + "adrian,josie";
        Assert.assertEquals(expectedResponse, listUserResponse);

    }

    private Map<String, String> extractResponseSetCookieHeaders(ChatResponse chatResponse) {
        return chatResponse.getHeaders().entrySet().stream()
                    .filter(entry -> entry.getKey().equals("Set-Cookie"))
                    .map(entry -> entry.getValue().split("="))
                    .collect(Collectors.toMap(key -> key[0], value -> value[1]));
    }
}
