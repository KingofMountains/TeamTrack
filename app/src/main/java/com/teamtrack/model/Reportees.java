package com.teamtrack.model;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class Reportees implements Parcelable {

    @SerializedName("employee_id")
    private String empId;
    @SerializedName("employee_name")
    private String empName;
    @SerializedName("emp_code")
    private String empCode;
    @SerializedName("designation_id")
    private String designationId;
    @SerializedName("reporting_to")
    private String reportingTo;
    @SerializedName("unique_ref_id")
    private String referenceID;
    @SerializedName("mobile")
    private String mobileNumber;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("last_updated_location")
    private String lastUpdatedLocation;

    protected Reportees(Parcel in) {
        empId = in.readString();
        empName = in.readString();
        empCode = in.readString();
        designationId = in.readString();
        reportingTo = in.readString();
        referenceID = in.readString();
        mobileNumber = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        lastUpdatedLocation = in.readString();
    }

    public static final Creator<Reportees> CREATOR = new Creator<Reportees>() {
        @Override
        public Reportees createFromParcel(Parcel in) {
            return new Reportees(in);
        }

        @Override
        public Reportees[] newArray(int size) {
            return new Reportees[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(empId);
        parcel.writeString(empName);
        parcel.writeString(empCode);
        parcel.writeString(designationId);
        parcel.writeString(reportingTo);
        parcel.writeString(referenceID);
        parcel.writeString(mobileNumber);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(lastUpdatedLocation);
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(String reportingTo) {
        this.reportingTo = reportingTo;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastUpdatedLocation() {
        return lastUpdatedLocation;
    }

    public void setLastUpdatedLocation(String lastUpdatedLocation) {
        this.lastUpdatedLocation = lastUpdatedLocation;
    }
}