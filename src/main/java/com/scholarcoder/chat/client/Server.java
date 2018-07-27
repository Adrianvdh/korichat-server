package com.scholarcoder.chat.client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    private ServerSocket serverSocket;
    private Thread serverThread;
    private ExecutorService connectionHandlerExecutorService;
    private List<Future> clientHandlerConnections;

    private volatile boolean serverStarted = false;
    private int port;

    public Server(int port) {
        this.port = port;
        connectionHandlerExecutorService = Executors.newCachedThreadPool();
        clientHandlerConnections = new ArrayList<>();
    }

    public static void main(String[] args) {
        new Server(31145).start();
    }

    public void start() {
        System.out.println("Starting client...");
        serverThread = new Thread(() -> {
            if (!tryBindServerSocket()) {
                return;
            }
            if(!Thread.currentThread().isInterrupted()) {
                System.out.println("Server is listening...");
            }
            notifyServerStarted();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    tryHandleConnection();
                }
                catch(SocketException e) {
                    return;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
        waitUntilStarted();
    }

    private void tryHandleConnection() throws IOException {
        Socket socket = serverSocket.accept();

        System.out.println("Handling new connection");
        Future submit = connectionHandlerExecutorService.submit(() -> {
            ClientHandler client = new ClientHandler(socket, generatedId(), this);
            client.handle();
        });
        clientHandlerConnections.add(submit);
    }

    private boolean tryBindServerSocket() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private synchronized void notifyServerStarted() {
        serverStarted = true;
        notify();
    }
    private synchronized void waitUntilStarted() {
        while(!serverStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    protected String generatedId() {
        return UUID.randomUUID().toString();
    }

    public void stop() {
        System.out.println("Stopping client");
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