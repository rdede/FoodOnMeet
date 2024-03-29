package com.example.foodonmeet.home;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.foodonmeet.User;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Event{
    private String title;
    private User user;
    private Date date;
    private String desc;
    private int nbGuests;
    private int nbGuestsMax;
    private String postId;
    private ArrayList<String> listGuests;
    private double latitude;
    private double longitude;

    public Event(String title, User user, Date date, String desc, int nbGuests, int nbGuestsMax, String postId, ArrayList<String> listGuests, double latitude, double longitude) {
        this.title = title;
        this.user = user;
        this.date = date;
        this.desc = desc;
        this.nbGuests = nbGuests;
        this.nbGuestsMax = nbGuestsMax;
        this.postId = postId;
        this.listGuests = listGuests;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        SimpleDateFormat fmtOut = new SimpleDateFormat("EEEE, dd");
        return fmtOut.format(date);
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimeString() {
        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
        return fmtOut.format(date);
    }

    public ArrayList<String> getListGuests() {
        return listGuests;
    }

    public void setListGuests(ArrayList<String> listGuests) {
        this.listGuests = listGuests;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
