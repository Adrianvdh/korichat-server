package com.scholarcoder.chat.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class ChatResponseTest {

    @Test
    public void testGetResponseAsString() {
        String expectedResponse = "CHAT/1.0 200 OK";

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setStatusCode(Responses.OK);

        ResponseService responseService = new ResponseService();
        String actualResponse = responseService.serializeAsString(chatResponse);

        Assert.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void testGetResponseAsString_WithHeadersOnly() {
        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "Header: some value\n"
                + "Header2: new Value";

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setStatusCode(Responses.OK);
        chatResponse.addHeader("Header", "some value");
        chatResponse.addHeader("Header2", "new Value");

        ResponseService responseService = new ResponseService();
        String actualResponse = responseService.serializeAsString(chatResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }


    @Test
    public void testGetResponseAsString_WithBodyOnly() {
        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "\n"
                + "Some body payload";

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setStatusCode(Responses.OK);
        chatResponse.setBody("Some body payload");

        ResponseService responseService = new ResponseService();
        String actualResponse = responseService.serializeAsString(chatResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetResponseAsString_WithHeadersAndBody() {
        String expectedResponse = "CHAT/1.0 200 OK\n"
                + "Header: some value\n"
                + "Header2: new Value\n"
                + "\n"
                + "Some body payload";

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setStatusCode(Responses.OK);
        chatResponse.addHeader("Header", "some value");
        chatResponse.addHeader("Header2", "new Value");
        chatResponse.setBody("Some body payload");

        ResponseService responseService = new ResponseService();
        String actualResponse = responseService.serializeAsString(chatResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

}
