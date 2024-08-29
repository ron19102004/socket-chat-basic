package org.example.entities;

public class RequestBuilder {
    private String action;
    private Object data;

    private RequestBuilder() {
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public RequestBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public RequestBuilder action(String action) {
        this.action = action;
        return this;
    }

    public Request build() {
        return new Request(action, data);
    }
}
