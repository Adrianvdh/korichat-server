package com.scholarcoder.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    private Server server;

    private String sessionId;
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private MessageProcessor messageProcessor;

    public ClientHandler(Socket clientSocket, String sessionId, Server server) {
        this.server = server;
        this.sessionId = sessionId;
        this.messageProcessor = new MessageProcessor();
        this.socket = clientSocket;

        trySetupInAndOutStreams(clientSocket);
    }

    private void trySetupInAndOutStreams(Socket clientSocket) {
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                if(Thread.currentThread().isInterrupted()) {
                    this.out.close();
                    this.in.close();
                    socket.close();
                    break;
                }
                String response = messageProcessor.process(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
