package com.scholarcoder.chat.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class ChatRequestTest {

    @Test
    public void testParseChatRequestWithRequestLineOnly() {
        String requestMessage = "REG adrian CHAT/1.0";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("REG", "adrian");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.parseRequestMessage(requestMessage);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);
    }

    @Test
    public void testParseRequestWithHeaders() {
        String requestMessage = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat");
        expectedChatRequest.putHeader("Invite", "josie:user,john:user");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.parseRequestMessage(requestMessage);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }

    @Test
    public void testParseRquestWithHeaderAndBody() {
        String requestMessage = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user\n" +
                "\n" +
                "Hello world";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat");
        expectedChatRequest.putHeader("Invite", "josie:user,john:user");
        expectedChatRequest.setBody("Hello world");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.parseRequestMessage(requestMessage);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }
}
