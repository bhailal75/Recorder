package com.app.spyapp.model;

import android.graphics.Bitmap;


public class ImageModel {

    private int id;
    private String displayName;
    private long imagedatemilies;
    private String imagePath;
    private String mimetype;
    private Bitmap bitmap;
    private long lastupdateDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getImagedatemilies() {
        return imagedatemilies;
    }

    public void setImagedatemilies(long imagedatemilies) {
        this.imagedatemilies = imagedatemilies;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public long getLastupdateDate() {
        return lastupdateDate;
    }

    public void setLastupdateDate(long lastupdateDate) {
        this.lastupdateDate = lastupdateDate;
    }
}
