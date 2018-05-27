package com.teamtrack.model.response;

import com.google.gson.annotations.SerializedName;
import com.teamtrack.model.Meetings;

import java.util.Collections;
import java.util.List;

/**
 * Created by GIRIN on 12-May-18.
 */

public class MeetingListResponse {

    @SerializedName("meeting_list")
    private List<Meetings> meetingsList = Collections.emptyList();


    public List<Meetings> getMeetingsList() {
        return meetingsList;
    }

    public void setMeetingsList(List<Meetings> meetingsList) {
        this.meetingsList = meetingsList;
    }
}
