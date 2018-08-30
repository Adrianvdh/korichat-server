package com.scholarcoder.chat.server.protocol;

import com.scholarcoder.chat.server.store.session.Session;
import com.scholarcoder.chat.server.store.session.SessionStore;
import com.scholarcoder.chat.server.store.session.SessionStoreSingelton;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class ChatRequestTest {

    @Test
    public void testParseChatRequestWithRequestLineOnly() {
        String requestMessage = "REG adrian CHAT/1.0";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("REG", "adrian");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.deserializeRequestMessage(requestMessage);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);
    }

    @Test
    public void testParseRequestWithHeaders() {
        String requestMessage = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user";

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("ADD_USER", "chat");
        expectedChatRequest.addHeader("Invite", "josie:user,john:user");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.deserializeRequestMessage(requestMessage);

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
        expectedChatRequest.addHeader("Invite", "josie:user,john:user");
        expectedChatRequest.setBody("Hello world");

        RequestService requestService = new RequestService();
        ChatRequest actualChatRequest = requestService.deserializeRequestMessage(requestMessage);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }


    @Test
    public void testGetSessionIdCookieFromRequest() {
        Session session = new Session();
        String sessionId = session.getSessionId();

        SessionStore sessionStore = SessionStoreSingelton.get();
        sessionStore.putSession(session);

        String requestMessage = "LIST_USER CHAT/1.0\n" +
                "Cookie: SESSIONID=" + sessionId;

        RequestService requestService = new RequestService(sessionStore);
        ChatRequest actualChatRequest = requestService.deserializeRequestMessage(requestMessage);

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("LIST_USER", "");
        expectedChatRequest.addHeader("Cookie", "SESSIONID=" + sessionId);
        expectedChatRequest.setSession(session);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }


    @Test
    public void testSerializeRequestAsString_WithHeadersAndBody() {
        ChatRequest chatRequest= new ChatRequest();
        chatRequest.setRequestLine("ADD_USER", "chat");
        chatRequest.addHeader("Invite", "josie:user,john:user");
        chatRequest.setBody("Hello world");

        String expectedRequest = "ADD_USER chat CHAT/1.0\n" +
                "Invite: josie:user,john:user\n" +
                "\n" +
                "Hello world";

        RequestService requestService = new RequestService();
        String actualRequest = requestService.serializeAsString(chatRequest);

        Assert.assertEquals(expectedRequest, actualRequest);
    }

    @Test
    public void testSerializeRequestAsString_WithMetaDataNotPresent() {
        ChatRequest chatRequest= new ChatRequest();
        chatRequest.setRequestLine("ADD_USER", "");
        chatRequest.addHeader("Invite", "josie:user,john:user");
        chatRequest.setBody("Hello world");

        String expectedRequest = "ADD_USER CHAT/1.0\n" +
                "Invite: josie:user,john:user\n" +
                "\n" +
                "Hello world";

        RequestService requestService = new RequestService();
        String actualRequest = requestService.serializeAsString(chatRequest);

        Assert.assertEquals(expectedRequest, actualRequest);
    }

    @Test
    public void testGetCookiesFromRequest() {
        String chatRequestString = "ADD_USER CHAT/1.0\n" +
                "Cookie: key=value\n" +
                "\n" +
                "Hello world";

        RequestService requestService = new RequestService();
        ChatRequest chatRequest = requestService.deserializeRequestMessage(chatRequestString);
        String actualCookieValue = chatRequest.getCookie("key");

        Assert.assertEquals("value", actualCookieValue);

    }
}
