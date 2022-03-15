package com.github.bogdan.model;

import java.util.Objects;

public class JsonMessage {
    private String message;
    private int status;

    public JsonMessage() {
    }

    public JsonMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
