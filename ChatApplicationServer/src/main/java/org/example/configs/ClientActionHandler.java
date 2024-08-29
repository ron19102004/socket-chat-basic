package org.example.configs;

import org.example.entities.Request;
import org.example.entities.ResponseBuilder;

public class ClientActionHandler {
    private final ClientHandler clientHandler;

    public ClientActionHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void handle(Request request) {
        switch (request.getAction()) {
            case "send-chat-messages" -> {
                this.clientHandler.broadcast(ResponseBuilder.builder()
                        .action(request.getAction())
                        .status(true)
                        .message("$["+this.clientHandler.getUsername()+"]:%"+request.getData().toString())
                        .build());
            }
            case "send-chat-emoji" ->{
                this.clientHandler.broadcast(ResponseBuilder.builder()
                        .action(request.getAction())
                        .status(true)
                        .message("$["+this.clientHandler.getUsername()+"]:&"+request.getData().toString())
                        .build());
            }
        }
    }
}
