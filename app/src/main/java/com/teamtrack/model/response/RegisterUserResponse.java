package com.teamtrack.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GIRIN on 12-May-18.
 */

public class RegisterUserResponse {

    @SerializedName("employee_id")
    private String empId;
    @SerializedName("employee_name")
    private String empName;
    @SerializedName("emp_code")
    private String emp_code;
    @SerializedName("designation_id")
    private String designation_id;
    @SerializedName("reporting_to")
    private String reporting_to;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("otp")
    private String otp;
    @SerializedName("unique_ref_id")
    private String unique_ref_id;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

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

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }

    public String getReporting_to() {
        return reporting_to;
    }

    public void setReporting_to(String reporting_to) {
        this.reporting_to = reporting_to;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUnique_ref_id() {
        return unique_ref_id;
    }

    public void setUnique_ref_id(String unique_ref_id) {
        this.unique_ref_id = unique_ref_id;
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
}
