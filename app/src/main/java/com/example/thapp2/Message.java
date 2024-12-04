package com.example.thapp2;

public class Message {
    private String senderId;
    private String message;
    private long timestamp;

    public Message() {} // Required for firestore

    public Message(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
