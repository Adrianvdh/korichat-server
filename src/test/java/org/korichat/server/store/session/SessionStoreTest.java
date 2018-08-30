package org.korichat.server.store.session;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

public class SessionStoreTest {

    SessionStore sessionStore;

    @Before
    public void setUp() throws Exception {
        sessionStore = SessionStoreSingelton.get();

        sessionStore.clear();
    }

    @Test
    public void getSessionStore() {
        Assert.assertNotNull(sessionStore);
    }

    @Test
    public void testSessionIdIsCreated() {
        Session session = new Session();

        final String sessionId = session.getSessionId();

        Assert.assertNotNull(sessionId);
    }

    @Test
    public void testCreateSession_andFindBySessionId() {
        Session session = new Session();
        session.setUsername("adrian");
        String sessionId = session.getSessionId();

        sessionStore.putSession(session);

        Session actualSession = sessionStore.findBySessionId(sessionId);

        Assert.assertEquals(session, actualSession);
    }

    @Test
    public void testFindByUsername() {
        Session session = new Session();
        session.setUsername("adrian");

        sessionStore.putSession(session);

        Session actualSession = sessionStore.findByUsername("adrian");

        Assert.assertEquals(session, actualSession);
    }

    @Test(expected = SessionNotFoundException.class)
    public void testFindBySessionId_sessionIdNotFound() {
        String randomId = UUID.randomUUID().toString();

        sessionStore.findBySessionId(randomId);

    }

    @Test(expected = SessionNotFoundException.class)
    public void testFindByUsername_sessionIdNotFound() {
        sessionStore.findByUsername("adrian");
    }

    @Test
    public void testClearAllSessions() {
        Session session = new Session();
        session.setUsername("adrian");
        sessionStore.putSession(session);

        Collection<Session> allSessions = sessionStore.findAll();

        Assert.assertEquals(1, allSessions.size());

        sessionStore.clear();
        allSessions = sessionStore.findAll();

        Assert.assertEquals(0, allSessions.size());
    }

}
