package org.korichat.server.api.user;

import org.junit.After;
import org.junit.Before;
import org.korichat.client.MessagingClient;
import org.korichat.server.Server;
import org.korichat.server.api.user.repository.UserRepository;
import org.korichat.server.api.user.repository.UserRepositorySingleton;
import org.korichat.server.repository.EmbeddedDatabaseBuilder;
import org.korichat.server.repository.HsqldbConnection;

public class ServerUserManagementTest {

    private static String HOST = "localhost";
    private static int PORT = 34567;

    private Server server;
    private MessagingClient client;

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.start();

        client = new MessagingClient(HOST, PORT);
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
}
