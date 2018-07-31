package com.scholarcoder.chat.server;

import com.scholarcoder.chat.server.processor.MessageProcessor;

import java.io.*;
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

                if (Thread.currentThread().isInterrupted()) {
                    this.out.close();
                    this.in.close();
                    socket.close();
                    break;
                }
                String response;
                try {
                    response = messageProcessor.process(inputLine);
                }
                catch (Throwable t) {
                    StringBuilder exceptionStringBuilder = new StringBuilder();
                    exceptionStringBuilder.append("500 Internal Server Error!").append(System.lineSeparator());

                    String stackTrace = convertStackTraceToString(t);
                    exceptionStringBuilder.append(stackTrace);

                    response = exceptionStringBuilder.toString();
                }
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String convertStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);

        return sw.toString();
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
