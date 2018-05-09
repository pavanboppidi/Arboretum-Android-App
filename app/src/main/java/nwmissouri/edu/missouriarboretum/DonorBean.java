package nwmissouri.edu.missouriarboretum;

import java.io.Serializable;

/**
 * Created by S518637 on 10/31/2014.
 */
public class DonorBean implements Serializable {

    //
    String donorTreeId;
    String donortrailID;
    String donorCname;
    String donorSname;
    String donorDescription;
    double donorLatitude;
    double donorLongitude;
    String donorWalkname;

    int DonorID;
    String donorFname;
    String donorLname;
    String companyname;

    public String getDonorTreeId() {
        return donorTreeId;
    }

    public void setDonorTreeId(String donorTreeId) {
        this.donorTreeId = donorTreeId;
    }

    public String getDonorWalkname() {
        return donorWalkname;
    }

    public void setDonorWalkname(String donorWalkname) {
        this.donorWalkname = donorWalkname;
    }

    public double getDonorLongitude() {
        return donorLongitude;
    }

    public void setDonorLongitude(double donorLongitude) {
        this.donorLongitude = donorLongitude;
    }

    public double getDonorLatitude() {
        return donorLatitude;
    }

    public void setDonorLatitude(double donorLatitude) {
        this.donorLatitude = donorLatitude;
    }

    public String getDonorDescription() {
        return donorDescription;
    }

    public void setDonorDescription(String donorDescription) {
        this.donorDescription = donorDescription;
    }

    public String getDonorSname() {
        return donorSname;
    }

    public void setDonorSname(String donorSname) {
        this.donorSname = donorSname;
    }

    public String getDonorCname() {
        return donorCname;
    }

    public void setDonorCname(String donorCname) {
        this.donorCname = donorCname;
    }

    public String getDonortrailID() {
        return donortrailID;
    }

    public void setDonortrailID(String donortrailID) {
        this.donortrailID = donortrailID;
    }

    public int getDonorID() {
        return DonorID;
    }

    public void setDonorID(int donorID) {
        DonorID = donorID;
    }

    public String getDonorFname() {
        return donorFname;
    }

    public void setDonorFname(String donorFname) {
        this.donorFname = donorFname;
    }

    public String getDonorLname() {
        return donorLname;
    }

    public void setDonorLname(String donorLname) {
        this.donorLname = donorLname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
}
