package com.pingpongchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    final int port;
    private ExecutorService executorService;

    public EchoServer(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    public static void main(String[] args) {
        new EchoServer(31145).start();
    }

    public void start() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            executorService.execute(() -> {
                try {
                    System.out.println("Echo Server started...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Handling new connection...");

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {

                        String uppercase = inputLine.toUpperCase();
                        out.println(uppercase);
                        System.out.println("Echo: " + uppercase);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        executorService.shutdown();
    }
}