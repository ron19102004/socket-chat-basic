package org.example.configs;

import org.example.entities.Response;

public interface IClientListener {
    void listen(Response response);
}
