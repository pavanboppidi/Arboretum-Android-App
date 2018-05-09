package nwmissouri.edu.missouriarboretum;

import java.io.Serializable;

public class TreeBean implements Serializable {

    String treeId;
    String trailId;
    String cName;
    String sName;
    String description;
    private double latitude;
    private double longitude;
    private String status;
    private String year;
    private String note;
    String audioUrl;
    String walkName;
    /**
     * Serial version id for runtime detection.
     */
    private static final long serialVersionUID = -4006145468965600749L;

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getTrailId() {
        return trailId;
    }

    public void setTrailId(String trailId) {
        this.trailId = trailId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getWalkName() {
        return walkName;
    }

    public void setWalkName(String walkName) {
        this.walkName = walkName;
    }

}
