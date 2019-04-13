package com.example.foodonmeet;

import android.graphics.Bitmap;

public class UserChat {

    private String name;
    private Bitmap profilePic;
    private String uid;

    public UserChat() {
    }

    public UserChat(String name, Bitmap profilePic) {
        this.name = name;
        this.profilePic = profilePic;
    }

    public UserChat(String name, String uid, Bitmap profilePic) {
        this.name = name;
        this.uid = uid;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
