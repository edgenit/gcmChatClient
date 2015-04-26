package com.example.chatdemo.database;

/**
 * Created by jeffreyfried on 4/24/15.
 */
public class ChatContact {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String name;
    private String email;
    private int count;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public ChatContact(String name, String email) {
        this.name = name;
        this.email = email;
        this.count = 0;
        this.id = -1;
    }

    @Override
    public String toString() {
        return this.email;
    }
}
