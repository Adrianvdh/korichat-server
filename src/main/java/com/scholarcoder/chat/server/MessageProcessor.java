package com.scholarcoder.chat.server;

import com.scholarcoder.chat.server.user.User;
import com.scholarcoder.chat.server.user.UserAlreadyExistsException;
import com.scholarcoder.chat.server.user.UserRepository;
import com.scholarcoder.chat.server.user.UserRepositorySingleton;

import java.util.List;
import java.util.stream.Collectors;

public class MessageProcessor {
    private UserRepository userRepository;

    public MessageProcessor() {
        this.userRepository = UserRepositorySingleton.get();
    }

    public String process(String message) {
        Payload payload = Payload.fromMessage(message);
        // TODO: Refactor to strategy pattern
        switch(payload.command) {
            case "REG":
                String username = payload.body;
                try {
                    userRepository.add(username);
                    return "201 Created";

                } catch (UserAlreadyExistsException e) {
                    return "409 Conflict";
                }

            case "LISTUSER":
                List<User> users = userRepository.findAll();
                String userListString = users.stream().map(User::getUsername).collect(Collectors.joining(","));
                if(userListString.isEmpty()) {
                    return "204 No Content";
                }
                return userListString;
        }

        return "405 Command Not Allowed";
    }



    static class Payload {
        public String command;
        public String body;

        public Payload(String command, String body) {
            this.command = command;
            this.body = body;
        }

        public static Payload fromMessage(String message) {

            String[] payloadString = message.split(" ");
            String command;
            if(payloadString.length == 1) {
                command = payloadString[0];
                return new Payload(command, "");
            }
            command = payloadString[0];
            String body = payloadString[1];

            return new Payload(command, body);
        }
    }
}
