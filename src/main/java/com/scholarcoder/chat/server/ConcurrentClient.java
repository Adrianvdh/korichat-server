package com.scholarcoder.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConcurrentClient {

    private static final String HOST = "localhost";
    private int port;

    public ConcurrentClient(int port) {
        this.port = port;
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 2; i++) {
           spinUpClient(i);
        }
    }

    static void spinUpClient(int number) {
        Thread client = new Thread(() -> new ConcurrentClient(11359).listenToServer());
        client.setName("Client " + number);
        client.start();

    }

    private void listenToServer() {
        try {
            String currentThread = Thread.currentThread().getName();

            Socket socket = new Socket(HOST, port);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(currentThread + " has started!!!");
            String input;
            while((input = bufferedReader.readLine()) != null) {
                System.out.println(currentThread + " -> " + input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
