package com.scholarcoder.chat.server;

import java.io.IOException;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    private ServerSocket serverSocket;
    private Thread serverThread;
    private ExecutorService connectionHandlerExecutorService;
    private List<Future> clientHandlerConnections;

    private volatile boolean started = false;
    private int port;

    public Server(int port) {
        this.port = port;
        connectionHandlerExecutorService = Executors.newCachedThreadPool();
        clientHandlerConnections = new ArrayList<>();
    }

    public void start() {
        System.out.println("Starting server...");
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if(!Thread.currentThread().isInterrupted()) {
                System.out.println("Server is listening...");
            }

            started = true;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();

                    System.out.println("Handling new connection");
                    Future submit = connectionHandlerExecutorService.submit(() -> {
                        ClientHandler client = new ClientHandler(socket, generatedId(), this);
                        client.handle();
                    });
                    clientHandlerConnections.add(submit);

                }
                catch(SocketException e) {
                    return;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        while(!started) { }
    }

    protected String generatedId() {
        return UUID.randomUUID().toString();
    }

    public void stop() {
        System.out.println("Stopping server");
        serverThread.interrupt();

        clientHandlerConnections.forEach(future -> future.cancel(true));
        connectionHandlerExecutorService.shutdown();

        try {
            if(serverSocket!=null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server shutdown");
    }
}