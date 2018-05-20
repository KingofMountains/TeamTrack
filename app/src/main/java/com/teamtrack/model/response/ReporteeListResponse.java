package com.teamtrack.model.response;

import com.google.gson.annotations.SerializedName;
import com.teamtrack.model.Reportees;

import java.util.Collections;
import java.util.List;

/**
 * Created by GIRIN on 12-May-18.
 */

public class ReporteeListResponse {

    @SerializedName("reporting_list")
    private List<Reportees> reportingList = Collections.emptyList();

    public List<Reportees> getReportingList() {
        return reportingList;
    }

    public void setReportingList(List<Reportees> reportingList) {
        this.reportingList = reportingList;
    }
}
