package com.teamtrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GIRIN on 26-May-18.
 */

public class Meetings implements Parcelable {

    @SerializedName("meeting_id")
    private String meetingID;
    @SerializedName("customer_id")
    private String customerID;
    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("customer_location_id")
    private String customerLocationID;
    @SerializedName("location_name")
    private String customerLocationName;
    @SerializedName("employee_id")
    private String employeeId;
    @SerializedName("schedule_date")
    private String scheduledDate;
    @SerializedName("meeting_from")
    private String meetingFromTime;
    @SerializedName("meeting_to")
    private String meetingToTime;
    @SerializedName("meeting_status")
    private String meetingStatus;
    @SerializedName("description")
    private String description;
    @SerializedName("status_updated_from")
    private String statusUpdatedFrom;
    @SerializedName("status_updated_on")
    private String statusUpdatedOn;
    @SerializedName("meeting_updates")
    private String meetingUpdates;

    protected Meetings(Parcel in) {
        meetingID = in.readString();
        customerID = in.readString();
        customerName = in.readString();
        customerLocationID = in.readString();
        customerLocationName = in.readString();
        employeeId = in.readString();
        scheduledDate = in.readString();
        meetingFromTime = in.readString();
        meetingToTime = in.readString();
        meetingStatus = in.readString();
        description = in.readString();
        statusUpdatedFrom = in.readString();
        statusUpdatedOn = in.readString();
        meetingUpdates = in.readString();
    }

    public static final Creator<Meetings> CREATOR = new Creator<Meetings>() {
        @Override
        public Meetings createFromParcel(Parcel in) {
            return new Meetings(in);
        }

        @Override
        public Meetings[] newArray(int size) {
            return new Meetings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(meetingID);
        parcel.writeString(customerID);
        parcel.writeString(customerName);
        parcel.writeString(customerLocationID);
        parcel.writeString(customerLocationName);
        parcel.writeString(employeeId);
        parcel.writeString(scheduledDate);
        parcel.writeString(meetingFromTime);
        parcel.writeString(meetingToTime);
        parcel.writeString(meetingStatus);
        parcel.writeString(description);
        parcel.writeString(statusUpdatedFrom);
        parcel.writeString(statusUpdatedOn);
        parcel.writeString(meetingUpdates);
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLocationID() {
        return customerLocationID;
    }

    public void setCustomerLocationID(String customerLocationID) {
        this.customerLocationID = customerLocationID;
    }

    public String getCustomerLocationName() {
        return customerLocationName;
    }

    public void setCustomerLocationName(String customerLocationName) {
        this.customerLocationName = customerLocationName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getMeetingFromTime() {
        return meetingFromTime;
    }

    public void setMeetingFromTime(String meetingFromTime) {
        this.meetingFromTime = meetingFromTime;
    }

    public String getMeetingToTime() {
        return meetingToTime;
    }

    public void setMeetingToTime(String meetingToTime) {
        this.meetingToTime = meetingToTime;
    }

    public String getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusUpdatedFrom() {
        return statusUpdatedFrom;
    }

    public void setStatusUpdatedFrom(String statusUpdatedFrom) {
        this.statusUpdatedFrom = statusUpdatedFrom;
    }

    public String getStatusUpdatedOn() {
        return statusUpdatedOn;
    }

    public void setStatusUpdatedOn(String statusUpdatedOn) {
        this.statusUpdatedOn = statusUpdatedOn;
    }

    public String getMeetingUpdates() {
        return meetingUpdates;
    }

    public void setMeetingUpdates(String meetingUpdates) {
        this.meetingUpdates = meetingUpdates;
    }
}
