package com.teamtrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.teamtrack.listeners.OnItemSelect;

import java.util.ArrayList;

public class Customer implements Parcelable, OnItemSelect{

    @SerializedName("customer_id")
    private String customerId;
    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("address")
    private ArrayList<CustomerLocation> locationList;

    protected Customer(Parcel in) {
        customerId = in.readString();
        customerName = in.readString();
        locationList = in.createTypedArrayList(CustomerLocation.CREATOR);
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(customerId);
        parcel.writeString(customerName);
        parcel.writeTypedList(locationList);
    }

    @Override
    public String getName() {
        return customerName;
    }

    @Override
    public String getId() {
        return customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<CustomerLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<CustomerLocation> locationList) {
        this.locationList = locationList;
    }
}