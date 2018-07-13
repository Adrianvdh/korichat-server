package com.scholarcoder.chat.server.transport;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRequestTest {

    @Test
    public void testParseChatRequestWithRequestLineOnly() {
        String request = "REG adrian CHAT/1.0";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("REG", "adrian", "CHAT/1.0");

        ChatRequest actualChatRequest = ChatRequest.fromRequest(request);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);
    }

    @Test
    public void testParseRequestWithHeaders() {
        String request = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat", "CHAT/1.0");
        expectedChatRequest.putHeader("Invite", "josie:user,john:user");

        ChatRequest actualChatRequest = ChatRequest.fromRequest(request);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }
}
