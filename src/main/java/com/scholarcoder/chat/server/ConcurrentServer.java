package com.scholarcoder.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentServer {

    private Thread serverThread;
    private ExecutorService connectionHandlerExecutorService;
    private List<Future> clientHandlerConnections;

    private int port;

    public ConcurrentServer(int port) {
        this.port = port;
        connectionHandlerExecutorService = Executors.newCachedThreadPool();
        clientHandlerConnections = new ArrayList<>();
    }

    public static void main(String[] args) {
        ConcurrentServer concurrentServer = new ConcurrentServer(11359);

        concurrentServer.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        concurrentServer.stop();

        concurrentServer = new ConcurrentServer(11359);
        concurrentServer.start();
    }

    public void start() {
        System.out.println("Stating server...");
        serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
                System.out.println("Server is listening...");
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("Handling new connection");
                        Future submit = connectionHandlerExecutorService.submit(() -> {
                            ClientHandler client = new ClientHandler(generatedId(), socket);
                            client.handle();
                        });
                        clientHandlerConnections.add(submit);

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

    private String generatedId() {
        return UUID.randomUUID().toString();
    }

    public void stop() {
        System.out.println("Stopping server");
        clientHandlerConnections.forEach(future -> future.cancel(true));
        connectionHandlerExecutorService.shutdown();
        serverThread.interrupt();

        System.out.println("Shutdown");
    }

    static class ClientHandler {
        private String sessionId;
        private Socket socket;

        public ClientHandler(String sessionId, Socket socket) {
            this.sessionId = sessionId;
            this.socket = socket;
        }

        public void handle() {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                while(!Thread.currentThread().isInterrupted()) {

                    printWriter.println("Msg from server. -> " + sessionId);
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
