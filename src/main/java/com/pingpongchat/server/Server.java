package com.pingpongchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.*;

public class Server {
    final int port;
    private ServerSocket serverSocket;
    private SocketListener socketListener;

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
            this.serverSocket = new ServerSocket(port);
            System.out.println("Awaiting connections...");

            socketListener = new SocketListener(this);
            socketListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SocketListener extends Thread {
        private boolean running = true;
        private Server server;

        public SocketListener(Server server) {
            this.server = server;
        }

        public void terminate() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                Socket clientSocket = acceptSocket(serverSocket);
                if (clientSocket == null) {
                    break;
                }
                String sessionId = generateSessionId();

                executorService.submit(() -> {
                    System.out.println("Handling new connection...");

                    ClientHandler clientHandler = new ClientHandler(clientSocket, sessionId, server);
                    clientConnections.put(sessionId, clientHandler);

                    clientHandler.handle();

                });

            }
        }

        private Socket acceptSocket(ServerSocket serverSocket) {
            try {
                return serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    protected String generateSessionId() {
        Random random = new Random();
        int randomInt = random.nextInt(999);

        NumberFormat numberFormat = new DecimalFormat("#000");
        return numberFormat.format(randomInt);
    }

    public void stop() {
        socketListener.terminate();
        try {
            socketListener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            if (executorService.isShutdown()) {
                tryCloseServerSocket();
                break;
            }
        }
    }

    private void tryCloseServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}