package com.scholarcoder.chat.server.transport;

import com.scholarcoder.chat.server.integration.ChatResponse;
import org.junit.Assert;
import org.junit.Test;

public class ChatResponseTest {

    @Test
    public void testParseResponse() {
        String response = "CHAT/1.O 201 Created";

        ChatResponse expectedChatResponse = new ChatResponse();
        expectedChatResponse.setStatusCode("201 Created");

        ChatResponse chatResponse = ChatResponse.fromResponse(response);

        Assert.assertEquals(expectedChatResponse, chatResponse);
    }

    @Test
    public void testParseChatResponseWithCookies() {
        String response = "CHAT/1.0 200 OK\n" +
                "Set-Cookie: someCookie=cookieValue";

        ChatResponse expectedChatResponse = new ChatResponse();
        expectedChatResponse.setStatusCode("200 OK");
        expectedChatResponse.addCookie("someCookie", "cookieValue");

        ChatResponse chatResponse = ChatResponse.fromResponse(response);

        Assert.assertEquals(expectedChatResponse, chatResponse);

    }

    @Test
    public void testParseChatResponseWithBody() {
        String response = "CHAT/1.0 200 OK\n" +
                "\n" +
                "adrian," +
                "josie";

        ChatResponse expectedChatResponse = new ChatResponse();
        expectedChatResponse.setStatusCode("200 OK");
        expectedChatResponse.setBody("adrian,josie");

        ChatResponse chatResponse = ChatResponse.fromResponse(response);

        Assert.assertEquals(expectedChatResponse, chatResponse);
    }
}
