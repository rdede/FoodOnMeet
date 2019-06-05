package com.example.foodonmeet.Chat;

public class ChatRoom {

    private String uid1;
    private String uid2;
    private String name;

    public ChatRoom(String uid1, String uid2, String name) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.name = name;
    }

    public ChatRoom(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid1() {
        return uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}