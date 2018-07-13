package com.scholarcoder.chat.server.integration;

import com.scholarcoder.chat.server.Server;
import com.scholarcoder.chat.server.user.UserRepositorySingleton;
import org.junit.After;
import org.junit.Before;

abstract class AbstractServerTest {

    protected static String HOST = "localhost";
    protected static int PORT = 34567;

    protected Server server;

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.start();

        UserRepositorySingleton.get().deleteAll();
    }

    @After
    public void tearDown() {
        server.stop();
    }

}
