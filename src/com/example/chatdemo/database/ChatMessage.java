package com.example.chatdemo.database;

import java.util.Date;

/**
 * Created by jeffreyfried on 4/8/15.
 */
public class ChatMessage {
    private String sender;
    private String receiver;
    private Date date;
    private String message;

    public ChatMessage(String sender, String receiver, Date date, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;

    }

    public String getReceiver() {
        return receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
