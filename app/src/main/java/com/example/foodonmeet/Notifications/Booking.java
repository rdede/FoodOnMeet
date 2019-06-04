package com.example.foodonmeet.Notifications;

public class Booking {

    private String name;
    private int flag;
    private String requesterUid;
    private String postName;
    private String postId;
    private String receiverUid;
    private String bookingId;

    public Booking(String name, int flag, String requesterUid, String postName, String postId, String receiverUid, String bookingId) {
        this.name = name;
        this.flag = flag;
        this.requesterUid = requesterUid;
        this.postName = postName;
        this.postId = postId;
        this.receiverUid = receiverUid;
        this.bookingId = bookingId;
    }

    public Booking() {
    }

    public String getRequesterUid() {
        return requesterUid;
    }

    public void setRequesterUid(String requesterUid) {
        this.requesterUid = requesterUid;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
