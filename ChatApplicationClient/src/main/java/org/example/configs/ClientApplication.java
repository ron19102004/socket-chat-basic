package org.example.configs;

import org.example.entities.Request;
import org.example.entities.RequestBuilder;
import org.example.entities.Response;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientApplication {
    private final Socket client;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    private ClientApplication(Socket client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),StandardCharsets.UTF_8));
    }

    public static ClientApplication newClient(String localhost, int port) throws IOException {
        Socket client = new Socket(localhost, port);
        return new ClientApplication(client);
    }
    public void closeClient() {
        try {
            this.client.close();
            this.writer.close();
            this.writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void send(Request request) throws IOException {
        this.writer.write(request.toJsonStr());
        this.writer.newLine();
        this.writer.flush();
    }

    public void listen(IClientListener clientListener) {
        new Thread(() -> {
            String jsonData;
            while (this.client.isConnected()) {
                try {
                    jsonData = this.reader.readLine();
                } catch (IOException e) {
                    break;
                }
                if (jsonData == null) {
                    break;
                }
                Response response = new Response(jsonData);
                clientListener.listen(response);
            }
        }).start();
    }
}
