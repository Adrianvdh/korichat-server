package com.pingpongchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GroupChatHandler implements ClientHandler {
    private Server server;

    private String sessionId;
    private PrintWriter out;
    private BufferedReader in;


    public GroupChatHandler(Socket clientSocket, String sessionId, Server server) {
        this.server = server;
        this.sessionId = sessionId;

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
                server.broadcastMessage(sessionId + " " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
