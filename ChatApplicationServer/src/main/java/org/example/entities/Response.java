package org.example.entities;

import org.json.JSONObject;

public class Response {
    private final String message;
    private final String action;
    private final boolean status;

    public Response(String message, String action, boolean status) {
        this.message = message;
        this.action = action;
        this.status = status;
    }
    public String toJsonStr(){
        JSONObject json = new JSONObject();
        json.put("message", this.message);
        json.put("action",this.action);
        json.put("status",this.status);
        return  json.toString();
    }
}
