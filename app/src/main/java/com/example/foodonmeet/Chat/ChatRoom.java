package com.example.foodonmeet.Chat;

import java.util.ArrayList;

public class ChatRoom {

    private String uid;
    private String name;

    public ChatRoom(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public ChatRoom() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}