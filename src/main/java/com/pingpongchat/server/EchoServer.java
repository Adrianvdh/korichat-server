package com.pingpongchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    final int port;
    private ExecutorService executorService;

    public EchoServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        new EchoServer(31145).start();
    }

    public void start() {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Awaiting connections...");
            while(true) {
                Socket clientSocket = acceptSocket(serverSocket);
                executorService.submit(() -> {
                    System.out.println("Handling new connection...");
                    SocketProcessor socketProcessor = new SocketProcessor(clientSocket);
                    socketProcessor.process();

                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        Thread serverThread = new Thread(() -> {
//        });
//        serverThread.start();
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