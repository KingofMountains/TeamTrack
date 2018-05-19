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
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.tasks.GetSchedulesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class SalesFragment extends Fragment implements OnItemSelectedListener {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    RecyclerView rvSchedules;
    MeetingsAdapter adapter;
    List<Meetings> scheduleList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment newInstance() {
        return new SalesFragment();
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
        init();
    }

    private void init() {

        rvSchedules = view.findViewById(R.id.rv_schedules);
        layoutManager = new LinearLayoutManager(thisActivity, LinearLayoutManager.VERTICAL, false);

        getMeetings();

    }

    private void getMeetings() {
        new GetSchedulesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Meetings>() {
            @Override
            public void onTaskCompleted(List<Meetings> list) {
                if (list != null) {
                    scheduleList = list;
                    adapter = new MeetingsAdapter(list, SalesFragment.this, "SALES");
                    rvSchedules.setAdapter(adapter);
                    rvSchedules.setLayoutManager(layoutManager);
                }
            }
        }).execute("");
    }

    @Override
    public void onItemSelected(int position) {
        if (mListener != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("selected_item", scheduleList.get(position));
            mListener.onFragmentInteraction("SCHEDULE_LIST_SELECT", bundle);
        }
    }
}

