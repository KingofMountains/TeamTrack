package com.teamtrack.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GIRIN on 12-May-18.
 */

public class RegisterUserResponse {

    @SerializedName("employee_id")
    private String empId;
    @SerializedName("employee_id")
    private String empName;
    @SerializedName("employee_type")
    private String empType;

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

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }
}
