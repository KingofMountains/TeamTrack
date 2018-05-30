package com.teamtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.Utilities.Preferences;
import com.teamtrack.adapters.MeetingsAdapter;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnItemSelectedListener;
import com.teamtrack.model.Meetings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class SalesMeetingsFragment extends Fragment implements OnItemSelectedListener {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    RecyclerView rvSchedules;
    MeetingsAdapter adapter;
    List<Meetings> meetingsList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    TextView tvNoDataFound;
    private int type = 1;

    public SalesMeetingsFragment() {
        // Required empty public constructor
    }

    public static SalesMeetingsFragment newInstance(int type, ArrayList<Meetings> meetingsList) {
        SalesMeetingsFragment fragment = new SalesMeetingsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putParcelableArrayList("meeting_list", meetingsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_schedules_sales, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
        init();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {

        Bundle extras = getArguments();

        rvSchedules = view.findViewById(R.id.rv_schedules);
        tvNoDataFound = view.findViewById(R.id.tv_no_data_found);
        layoutManager = new LinearLayoutManager(thisActivity, LinearLayoutManager.VERTICAL, false);

        if (extras != null) {
            if (extras.containsKey("type")) {
                type = extras.getInt("type");
            }
            if (extras.containsKey("meeting_list")) {
                meetingsList = extras.getParcelableArrayList("meeting_list");
                meetingsList = getMeetingList(meetingsList, type);
            }
        }

        if (meetingsList != null && meetingsList.size() > 0) {
            adapter = new MeetingsAdapter(meetingsList, SalesMeetingsFragment.this);
            rvSchedules.setAdapter(adapter);
            rvSchedules.setLayoutManager(layoutManager);
            rvSchedules.setVisibility(View.VISIBLE);
            tvNoDataFound.setVisibility(View.GONE);
        } else {
            rvSchedules.setVisibility(View.GONE);
            tvNoDataFound.setVisibility(View.VISIBLE);
        }

    }

    private List<Meetings> getMeetingList(List<Meetings> meeting_list, int type) {

        List<Meetings> currentList = new ArrayList<>();

        for (Meetings meeting : meeting_list) {
            if (validateMeeting(meeting, type) != null) {
                if (type == 0) {
                    meeting.setMeetingStatus("Meeting Completed");
                }
                currentList.add(meeting);
            }
        }

        return currentList;
    }

    private Meetings validateMeeting(Meetings meeting, int type) {

        String myFormat = "dd MMM yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            Date selectedDate = sdf.parse(meeting.getScheduledDate());
            Date currentDate = sdf.parse(sdf.format(Calendar.getInstance().getTime()));

            if (type == 0) {
                return selectedDate.before(currentDate) ? meeting : null;
            } else if (type == 1) {
                return selectedDate.equals(currentDate) ? meeting : null;
            } else if (type == 2) {
                return selectedDate.after(currentDate) ? meeting : null;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onItemSelected(int position, String action) {

        if (mListener != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("selected_item", meetingsList.get(position));
            if (!Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE).equalsIgnoreCase("MANAGER")) {
                mListener.onFragmentInteraction("SCHEDULE_LIST_SELECT", bundle);
            } else {
                mListener.onFragmentInteraction("UPDATE_MEETING", bundle);
            }
        }
    }
}

