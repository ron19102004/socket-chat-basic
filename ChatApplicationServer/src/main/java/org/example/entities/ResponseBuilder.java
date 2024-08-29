package org.example.entities;

public class ResponseBuilder {
    private String message;
    private String action;
    private boolean status;

    private ResponseBuilder() {
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public ResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResponseBuilder action(String action) {
        this.action = action;
        return this;
    }

    public ResponseBuilder status(boolean status) {
        this.status = status;
        return this;
    }
    public Response build(){
        return new Response(message,action,status);
    }
}
