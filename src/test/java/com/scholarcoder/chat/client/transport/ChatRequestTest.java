package com.scholarcoder.chat.client.transport;

import com.scholarcoder.chat.client.store.session.Session;
import com.scholarcoder.chat.client.store.session.SessionStore;
import com.scholarcoder.chat.client.store.session.SessionStoreSingelton;
import org.junit.Assert;
import org.junit.Test;

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
    public void testGetSessionFromRequest() {
        Session session = new Session();
        String sessionId = session.getSessionId();

        SessionStore sessionStore = SessionStoreSingelton.get();
        sessionStore.putSession(session);

        String requestMessage = "LISTUSER CHAT/1.0\n" +
                "SESSIONID: " + sessionId;

        RequestService requestService = new RequestService(sessionStore);
        ChatRequest actualChatRequest = requestService.deserializeRequestMessage(requestMessage);

        ChatRequest expectedChatRequest = new ChatRequest();
        expectedChatRequest.setRequestLine("LISTUSER", "");
        expectedChatRequest.addHeader("SESSIONID", sessionId);
        expectedChatRequest.setSession(session);

        Assert.assertEquals(expectedChatRequest, actualChatRequest);

    }



}
