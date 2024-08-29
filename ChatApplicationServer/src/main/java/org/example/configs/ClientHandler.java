package org.example.configs;

import org.example.entities.Request;
import org.example.entities.Response;
import org.example.entities.ResponseBuilder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private String username;
    private final Socket client;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    public String getUsername() {
        return username;
    }

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),StandardCharsets.UTF_8));
    }

    private void init() throws IOException {
        this.send(ResponseBuilder.builder()
                .message("Connected. Welcome you!")
                .action("system")
                .status(true)
                .build());
        String jsonData = this.reader.readLine();
        if (jsonData == null) return;
        Request request = new Request(jsonData);
        this.username = (String) request.getData();
        this.broadcast(ResponseBuilder.builder()
                .message("[" + this.username + " is Online]")
                .action("system")
                .status(true)
                .build());
        System.out.println("Client connected: " + this.username);
    }

    @Override
    public void run() {
        try {
            this.init();
        } catch (IOException e) {
            try {
                this.offline();
            } catch (IOException ex) {
                return;
            }
            return;
        }
        ClientActionHandler clientActionHandler = new ClientActionHandler(this);
        String jsonData;
        while (this.client.isConnected()) {
            try {
                jsonData = this.reader.readLine();
                if (jsonData == null) break;
                Request request = new Request(jsonData);
                clientActionHandler.handle(request);
            } catch (IOException e) {
                break;
            }
        }
        try {
            this.offline();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void offline() throws IOException {
        this.broadcast(ResponseBuilder.builder()
                .message("[" + this.username + " is Offline]")
                .action("system")
                .status(true)
                .build());
        ServerConfiguration.removeClientOutOfClients(this);
        System.out.println("Client close: " + this.username);

    }

    public void send(Response response) throws IOException {
        this.writer.write(response.toJsonStr());
        this.writer.newLine();
        this.writer.flush();
    }

    public void broadcast(Response response) {
        ServerConfiguration.getClients().forEach(clientHandler -> {
            if (!clientHandler.client.equals(this.client)) {
                try {
                    clientHandler.send(response);
                } catch (IOException e) {
                    System.out.println("Error:" + e.getMessage());
                }
            }
        });
    }
}
