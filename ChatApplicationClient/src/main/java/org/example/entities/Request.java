package org.example.entities;

import org.json.JSONObject;

public class Request {
    private final String action;
    private final Object data;

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }
    public String toJsonStr(){
        JSONObject json = new JSONObject();
        json.put("action", this.action);
        json.put("data", this.data);
        return json.toString();
    }
}
