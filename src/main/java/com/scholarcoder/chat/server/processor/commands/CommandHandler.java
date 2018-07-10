package com.scholarcoder.chat.server.processor.commands;

public interface CommandHandler {
    boolean applicable(String action);

    String doPerform(String payloadBody);
}
