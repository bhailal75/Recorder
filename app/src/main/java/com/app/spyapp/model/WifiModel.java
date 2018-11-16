package com.app.spyapp.model;


public class WifiModel {
    private String ssID;
    private  String BSSID;
    private String MAC;
    private String state;
    private String RSSI;
    private String LinkSpeed;
    private String Frequency;
    private String NetID;
    private String score;

    public String getSsID() {
        return ssID;
    }

    public void setSsID(String ssID) {
        this.ssID = ssID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getLinkSpeed() {
        return LinkSpeed;
    }

    public void setLinkSpeed(String linkSpeed) {
        LinkSpeed = linkSpeed;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public String getNetID() {
        return NetID;
    }

    public void setNetID(String netID) {
        NetID = netID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
