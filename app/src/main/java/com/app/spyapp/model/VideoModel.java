package com.app.spyapp.model;


public class VideoModel {

    private int id;
    private String videopath;
    private String mimeType;
    private String title;
    private String category;
    private long duration;
    private long addeddate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(long addeddate) {
        this.addeddate = addeddate;
    }
}
