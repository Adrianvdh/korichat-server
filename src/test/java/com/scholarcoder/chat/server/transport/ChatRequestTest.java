package com.scholarcoder.chat.server.transport;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRequestTest {

    @Test
    public void testParseChatRequestWithRequestLineOnly() {
        String request = "REG adrian CHAT/1.0";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("REG", "adrian");

        ChatRequest actualChatRequest = ChatRequest.fromRequest(request);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);
    }

    @Test
    public void testParseRequestWithHeaders() {
        String request = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat");
        expectedChatRequest.putHeader("Invite", "josie:user,john:user");

        ChatRequest actualChatRequest = ChatRequest.fromRequest(request);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }

    @Test
    public void testParseRquestWithHeaderAndBody() {
        String request = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user\n" +
                "\n" +
                "Hello world";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat");
        expectedChatRequest.putHeader("Invite", "josie:user,john:user");
        expectedChatRequest.setBody("Hello world");

        ChatRequest actualChatRequest = ChatRequest.fromRequest(request);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }

    @Ignore
    @Test(expected = InvalidChatRequestException.class)
    public void testParseInvalidRequest() {
        String request = " chat CHAT/1.0\n" +
                "Invite: \n" +
                "\n" +
                "Hello world";

        ChatRequest.fromRequest(request);
    }
}
