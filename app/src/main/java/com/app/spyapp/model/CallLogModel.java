package com.app.spyapp.model;


public class CallLogModel {
    private String calllogId;
    private String username;
    private String phnNumber;
    private String callType;
    private String callDate;
    private String callDuration;
    private String dir;
    private int dircode;

    public String getCalllogId() {
        return calllogId;
    }

    public void setCalllogId(String calllogId) {
        this.calllogId = calllogId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhnNumber() {
        return phnNumber;
    }

    public void setPhnNumber(String phnNumber) {
        this.phnNumber = phnNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getDircode() {
        return dircode;
    }

    public void setDircode(int dircode) {
        this.dircode = dircode;
    }
}
