package org.example.entities;

import org.json.JSONObject;

public class Response {
    private final String message;
    private final String action;
    private final boolean status;

    public Response(String jsonData) {
        JSONObject json = new JSONObject(jsonData);
        this.message = json.getString("message");
        this.action = json.getString("action");
        this.status = json.getBoolean("status");
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public boolean isStatus() {
        return status;
    }
}
