package com.scholarcoder.chat.server;

import org.junit.Assert;
import org.junit.Test;

public class ServerTest {

    @Test
    public void testRandomNumberGenerator() {
        String regexMatcherForRandomNumber = "\\d{3}";
        Server server = new Server(0);

        String randomSessionId = server.generateSessionId();

        Assert.assertTrue(randomSessionId.matches(regexMatcherForRandomNumber));
    }
}
