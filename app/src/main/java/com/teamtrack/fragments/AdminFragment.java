package com.teamtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamtrack.R;
import com.teamtrack.adapters.SchedulesAdapter;
import com.teamtrack.database.tables.Schedule;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnItemSelectedListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.tasks.GetSchedulesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class AdminFragment extends Fragment implements OnItemSelectedListener{

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    FloatingActionButton floatButtonAddSchedule;
    RecyclerView rvSchedules;
    SchedulesAdapter adapter;
    List<Schedule> scheduleList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_admin, container, false);
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

    private void init() {

        floatButtonAddSchedule = view.findViewById(R.id.float_btn_add_schedule);
        rvSchedules = view.findViewById(R.id.rv_schedules_admin);
        layoutManager = new LinearLayoutManager(thisActivity, LinearLayoutManager.VERTICAL, false);

        new GetSchedulesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Schedule>() {
            @Override
            public void onTaskCompleted(List<Schedule> list) {
                scheduleList = list;
                adapter = new SchedulesAdapter(list, AdminFragment.this,"ADMIN");
                rvSchedules.setAdapter(adapter);
                rvSchedules.setLayoutManager(layoutManager);
            }
        }).execute("");

        floatButtonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFragmentInteraction("ADD_SCHEDULE");
                }
            }
        });

    }

    @Override
    public void onItemSelected(int position) {

    }

}

