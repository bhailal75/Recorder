package com.app.spyapp.model;


public class BrowserModel {
    private String bookmarkid;
    private String title;
    private String url;
    private String type;
    private String datetime;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookmarkid() {
        return bookmarkid;
    }

    public void setBookmarkid(String bookmarkid) {
        this.bookmarkid = bookmarkid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
