package com.app.spyapp.model;

import android.graphics.Bitmap;

public class MMSModel {
    private String msgId;
    private String msgTextContent;
    private String msgImagePath;
    private String address;
    private String date;
    private String type;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTextContent() {
        return msgTextContent;
    }

    public void setMsgTextContent(String msgTextContent) {
        this.msgTextContent = msgTextContent;
    }

    public String getMsgImagePath() {
        return msgImagePath;
    }

    public void setMsgImagePath(String msgImagePath) {
        this.msgImagePath = msgImagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
