package org.example.configs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConfiguration {
    private static ArrayList<ClientHandler> CLIENTS;
    private final ServerSocket server;
    private ServerConfiguration(ServerSocket server){
        this.server = server;
        CLIENTS = new ArrayList<>();
    }
    public static ServerConfiguration initializer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        return new ServerConfiguration(server);
    }
    public void listen(IClientConnected clientConnected) throws IOException {
        while (!this.server.isClosed()){
            Socket client = this.server.accept();
            clientConnected.action(client);
            ClientHandler clientHandler = new ClientHandler(client);
            new Thread(clientHandler).start();
            CLIENTS.add(clientHandler);
        }
    }
    public static ArrayList<ClientHandler> getClients(){
        return CLIENTS;
    }
    public static void removeClientOutOfClients(ClientHandler clientHandler){
        CLIENTS.remove(clientHandler);
    }
}
