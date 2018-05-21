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

import com.teamtrack.R;
import com.teamtrack.adapters.MeetingsAdapter;
import com.teamtrack.database.tables.Meetings;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

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
    private int type = 0;

    public SalesMeetingsFragment() {
        // Required empty public constructor
    }

    public static SalesMeetingsFragment newInstance(int type, ArrayList<Meetings> meetingsList) {
        SalesMeetingsFragment fragment = new SalesMeetingsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putParcelableArrayList("meeting_list",meetingsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_sales, container, false);
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
        if (extras != null) {
            if (extras.containsKey("type")) {
                type = getArguments().getInt("type");
            }
            if (extras.containsKey("meeting_list")) {
                meetingsList = getArguments().getParcelableArrayList("meeting_list");
            }
        }

        rvSchedules = view.findViewById(R.id.rv_schedules);
        layoutManager = new LinearLayoutManager(thisActivity, LinearLayoutManager.VERTICAL, false);

        if (meetingsList != null) {
            adapter = new MeetingsAdapter(meetingsList, SalesMeetingsFragment.this, "SALES");
            rvSchedules.setAdapter(adapter);
            rvSchedules.setLayoutManager(layoutManager);
        }

    }

    @Override
    public void onItemSelected(int position, String action) {
        if (mListener != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("selected_item", meetingsList.get(position));
            mListener.onFragmentInteraction("SCHEDULE_LIST_SELECT", bundle);
        }
    }


}

