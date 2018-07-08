package com.pingpongchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    final int port;
    private Thread serverThread;
    private ExecutorService connectionHandlerExecutorService;
    private List<Future> connectionFutures;


    public Server(int port) {
        this.port = port;
        this.connectionHandlerExecutorService = Executors.newCachedThreadPool();
        this.connectionFutures = new ArrayList<>();
    }

    public void start() {
        serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server is listening...");
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("Handling new connection");
                        Future clientHandlerTask = connectionHandlerExecutorService.submit(() -> {
                            ClientHandler client = new ClientHandler(socket, generateSessionId(), this);
                            client.handle();
                        });
                        connectionFutures.add(clientHandlerTask);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                serverSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        serverThread.start();
    }

    protected String generateSessionId() {
        Random random = new Random();
        int randomInt = random.nextInt(999);

        NumberFormat numberFormat = new DecimalFormat("#000");
        return numberFormat.format(randomInt);
    }

    public void stop() {
        serverThread.interrupt();
        connectionFutures.forEach(future -> future.cancel(true));
        connectionHandlerExecutorService.shutdown();

    }

    private void tryCloseServerSocket() {
//        try {
//            System.out.println("Closing server socket");
//            serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}