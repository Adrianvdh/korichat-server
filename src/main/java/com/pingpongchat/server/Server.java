package com.pingpongchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    final int port;
    private ExecutorService executorService;
    private ConcurrentMap<String, ClientHandler> clientConnections;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.clientConnections = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        new Server(31145).start();
    }

    public void start() {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Awaiting connections...");
            while(true) {
                Socket clientSocket = acceptSocket(serverSocket);

                String sessionId = generateSessionId();

                executorService.submit(() -> {
                    System.out.println("Handling new connection...");

                    ClientHandler clientHandler = new GroupChatHandler(clientSocket, sessionId,this);
                    clientConnections.put(sessionId, clientHandler);

                    clientHandler.handle();

                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        clientConnections.forEach((sessionId, clientHandler) -> clientHandler.sendMessage(message));
    }

    protected String generateSessionId() {
        Random random = new Random();
        int randomInt = random.nextInt(999);

        NumberFormat numberFormat = new DecimalFormat("#000");
        return numberFormat.format(randomInt);
    }


    private Socket acceptSocket(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stop() {
        executorService.shutdown();
    }
}