package com.teamtrack.model.response;

import com.google.gson.annotations.SerializedName;
import com.teamtrack.model.Customer;

import java.util.Collections;
import java.util.List;

/**
 * Created by GIRIN on 12-May-18.
 */

public class CustomerListResponse {

    @SerializedName("details")
    private List<Customer> customerList = Collections.emptyList();


    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
