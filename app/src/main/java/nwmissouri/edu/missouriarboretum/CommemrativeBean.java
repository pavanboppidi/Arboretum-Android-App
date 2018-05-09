package nwmissouri.edu.missouriarboretum;

import java.io.Serializable;

/**
 * Created by S518620 on 10/30/2014.
 */
public class CommemrativeBean implements Serializable {


    String commemrativeTreeId;
    String commemrativetrailId;
    String commemrativeCname;
    String commemrativeSname;
    String commemrativeDescription;
    double commemrativeLatitude;
    double commemrativelongitude;
    String commemrativeWalkname;
    int commId;
    String Type;
    String commFname;
    String commLname;
    String commCompany;
    String commtreeId;
    int donorID;

    public String getCommemrativeTreeId() {
        return commemrativeTreeId;
    }

    public void setCommemrativeTreeId(String commemrativeTreeId) {
        this.commemrativeTreeId = commemrativeTreeId;
    }

    public String getCommemrativeWalkname() {
        return commemrativeWalkname;
    }

    public void setCommemrativeWalkname(String commemrativeWalkname) {
        this.commemrativeWalkname = commemrativeWalkname;
    }

    public double getCommemrativelongitude() {
        return commemrativelongitude;
    }

    public void setCommemrativelongitude(double commemrativelongitude) {
        this.commemrativelongitude = commemrativelongitude;
    }

    public double getCommemrativeLatitude() {
        return commemrativeLatitude;
    }

    public void setCommemrativeLatitude(double commemrativeLatitude) {
        this.commemrativeLatitude = commemrativeLatitude;
    }

    public String getCommemrativeDescription() {
        return commemrativeDescription;
    }

    public void setCommemrativeDescription(String commemrativeDescription) {
        this.commemrativeDescription = commemrativeDescription;
    }

    public String getCommemrativeSname() {
        return commemrativeSname;
    }

    public void setCommemrativeSname(String commemrativeSname) {
        this.commemrativeSname = commemrativeSname;
    }

    public String getCommemrativeCname() {
        return commemrativeCname;
    }

    public void setCommemrativeCname(String commemrativeCname) {
        this.commemrativeCname = commemrativeCname;
    }

    public String getCommemrativetrailId() {
        return commemrativetrailId;
    }

    public void setCommemrativetrailId(String commemrativetrailId) {
        this.commemrativetrailId = commemrativetrailId;
    }

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCommFname() {
        return commFname;
    }

    public void setCommFname(String commFname) {
        this.commFname = commFname;
    }

    public String getCommLname() {
        return commLname;
    }

    public void setCommLname(String commLname) {
        this.commLname = commLname;
    }

    public String getCommCompany() {
        return commCompany;
    }

    public void setCommCompany(String commCompany) {
        this.commCompany = commCompany;
    }

    public String getCommtreeId() {
        return commtreeId;
    }

    public void setCommtreeId(String commtreeId) {
        this.commtreeId = commtreeId;
    }

    public int getDonorID() {
        return donorID;
    }

    public void setDonorID(int donorID) {
        this.donorID = donorID;
    }
}
