package com.teamtrack.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Schedule implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    private int scheduleId;
    @ColumnInfo(name = "customerName")
    private String customerName;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "assignedTo")
    private String assignedTo;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "radius_limit")
    private int radiusLimit;
    @ColumnInfo(name = "remarks")
    private String remarks;
    @ColumnInfo(name = "sales_location")
    private String sales_location;
    @ColumnInfo(name = "sales_location_time")
    private String sales_location_time;

    protected Schedule(Parcel in) {
        scheduleId = in.readInt();
        customerName = in.readString();
        location = in.readString();
        assignedTo = in.readString();
        description = in.readString();
        status = in.readString();
        radiusLimit = in.readInt();
        remarks = in.readString();
        sales_location = in.readString();
        sales_location_time = in.readString();
    }

    public Schedule() {
        //empty constructor
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(scheduleId);
        dest.writeString(customerName);
        dest.writeString(location);
        dest.writeString(assignedTo);
        dest.writeString(description);
        dest.writeString(status);
        dest.writeInt(radiusLimit);
        dest.writeString(remarks);
        dest.writeString(sales_location);
        dest.writeString(sales_location_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRadiusLimit() {
        return radiusLimit;
    }

    public void setRadiusLimit(int radiusLimit) {
        this.radiusLimit = radiusLimit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSales_location() {
        return sales_location;
    }

    public void setSales_location(String sales_location) {
        this.sales_location = sales_location;
    }

    public String getSales_location_time() {
        return sales_location_time;
    }

    public void setSales_location_time(String sales_location_time) {
        this.sales_location_time = sales_location_time;
    }
}