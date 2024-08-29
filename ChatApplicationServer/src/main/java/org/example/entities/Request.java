package org.example.entities;

import org.json.JSONObject;

public class Request {
    private final String action;
    private final Object data;
    public Request(String jsonData){
        JSONObject json = new JSONObject(jsonData);
        this.action = json.getString("action");
        this.data = json.getString("data");
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }
}
