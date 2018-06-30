package com.pingpongchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketProcessor {
    Socket clientSocket;
    public SocketProcessor(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void process() {
        try {

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

    }
}
