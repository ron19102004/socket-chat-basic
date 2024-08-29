package org.example.configs;

import org.example.entities.Request;

import java.net.Socket;

public interface IClientConnected {
    void action(Socket client);
}
