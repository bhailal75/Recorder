package com.app.spyapp.model;


public class CallRecordModel {
    private String recordId;
    private String incomingnumber;
    private String outgoingnumber;
    private String recorddate;
    private String filename;
    private String filepath;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getIncomingnumber() {
        return incomingnumber;
    }

    public void setIncomingnumber(String incomingnumber) {
        this.incomingnumber = incomingnumber;
    }

    public String getOutgoingnumber() {
        return outgoingnumber;
    }

    public void setOutgoingnumber(String outgoingnumber) {
        this.outgoingnumber = outgoingnumber;
    }

    public String getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(String recorddate) {
        this.recorddate = recorddate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
