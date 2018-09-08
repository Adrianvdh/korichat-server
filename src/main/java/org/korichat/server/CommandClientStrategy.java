package org.korichat.server;

import org.korichat.server.processor.MessageProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommandClientStrategy implements HandlerStrategy<String> {

    private MessageProcessor messageProcessor = new MessageProcessor();
    private PrintWriter out;

    public CommandClientStrategy(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void handle(String request) {
        String response;
        try {
            response = messageProcessor.process(request);
        } catch (Throwable t) {
            StringBuilder exceptionStringBuilder = new StringBuilder();
            exceptionStringBuilder.append("500 Internal Server Error!").append(System.lineSeparator());

            String stackTrace = convertStackTraceToString(t);
            exceptionStringBuilder.append(stackTrace);

            response = exceptionStringBuilder.toString();
        }
        out.println(response);
    }

    private String convertStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);

        return sw.toString();
    }

}
