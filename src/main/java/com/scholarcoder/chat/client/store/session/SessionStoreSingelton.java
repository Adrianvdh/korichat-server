package com.scholarcoder.chat.client.store.session;

public class SessionStoreSingelton {

    private SessionStore sessionStore = new SessionStore();
    private static SessionStoreSingelton instance = new SessionStoreSingelton();

    private SessionStoreSingelton() {
    }


    public static SessionStore get() {
        return instance.sessionStore;
    }
}
