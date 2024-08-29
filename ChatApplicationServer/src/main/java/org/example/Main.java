package org.example;

import org.example.configs.ServerConfiguration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerConfiguration.initializer(2004)
                .listen(client -> {
                    System.out.println(client.getInetAddress());
                });
    }
}