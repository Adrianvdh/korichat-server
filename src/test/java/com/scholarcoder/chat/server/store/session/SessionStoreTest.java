package com.scholarcoder.chat.server.store.session;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Future;

public class SessionStoreTest {

    @Test
    public void getSessionStore() {
        SessionStore sessionStore = SessionStoreSingelton.get();

        Assert.assertNotNull(sessionStore);
    }

    @Test
    public void testPutSession() {
        SessionStore sessionStore = new SessionStore();

        Session session = new Session("adrian");
        Future mockFuture = Mockito.mock(Future.class);

        sessionStore.putConnectionHandle(session, mockFuture);

        Future clientHandle = sessionStore.getClientHandle(session);

        Assert.assertEquals(mockFuture, clientHandle);
    }

    @Test
    public void testHotswapSessionId() {
        Session currentSession = new Session("adrian");
        Session newSession = new Session("josie");
        Future mockFuture = Mockito.mock(Future.class);
        SessionStore sessionStore = new SessionStore();

        sessionStore.putConnectionHandle(currentSession, mockFuture);

        sessionStore.hotswapSessionId(currentSession, newSession);

        Future clientHandle = sessionStore.getClientHandle(newSession);

        Assert.assertEquals(mockFuture, clientHandle);
        assertOriginalSessionDoesNotExist(currentSession, sessionStore);
    }

    private void assertOriginalSessionDoesNotExist(Session currentSession, SessionStore sessionStore) {
        awaitAssertion(1000, () -> Assert.assertNull(sessionStore.getClientHandle(currentSession)));
    }

    private void awaitAssertion(int waitPeriod, Runnable runnable) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(waitPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
        });
        thread.start();
    }

}
