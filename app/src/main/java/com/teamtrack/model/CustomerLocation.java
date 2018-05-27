package com.teamtrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.teamtrack.listeners.OnItemSelect;

public class CustomerLocation implements Parcelable,OnItemSelect{

    @SerializedName("customer_location_id")
    private String customerLocationId;
    @SerializedName("location_name")
    private String customerLocationName;

    protected CustomerLocation(Parcel in) {
        customerLocationId = in.readString();
        customerLocationName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerLocationId);
        dest.writeString(customerLocationName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerLocation> CREATOR = new Creator<CustomerLocation>() {
        @Override
        public CustomerLocation createFromParcel(Parcel in) {
            return new CustomerLocation(in);
        }

        @Override
        public CustomerLocation[] newArray(int size) {
            return new CustomerLocation[size];
        }
    };

    @Override
    public String getName() {
        return customerLocationName;
    }

    @Override
    public String getId() {
        return customerLocationId;
    }
}