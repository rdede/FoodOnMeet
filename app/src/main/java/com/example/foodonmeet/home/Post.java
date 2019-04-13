package com.example.foodonmeet.home;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.foodonmeet.User;

import java.util.Date;

public class Post {
    private Bitmap bitmap;
    private String title;
    private int userId;
    private String date;
    private int time;
    private int nbGuests;
    private int nbGuestsMax;
    private String postId;

    public Post(String title, int userId, String date, int time, int nbGuests/*, Bitmap bitmap*/,int nbGuestsMax, String postId) {
        this.title = title;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.nbGuests = nbGuests;
        this.nbGuestsMax = nbGuestsMax;
        /*this.bitmap = bitmap;*/
        this.postId = postId;
    }

    public Post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNbGuests() {
        return nbGuests;
    }

    public void setNbGuests(int nbGuests) {
        this.nbGuests = nbGuests;
    }

    public int getNbGuestsMax() {
        return nbGuestsMax;
    }

    public void setNbGuestsMax(int nbGuestsMax) {
        this.nbGuestsMax = nbGuestsMax;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
