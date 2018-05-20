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
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.Utilities.Preferences;
import com.teamtrack.adapters.ReporteesAdapter;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnItemSelectedListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Reportees;
import com.teamtrack.tasks.GetReporteesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class AdminFragment extends Fragment implements OnItemSelectedListener {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    FloatingActionButton floatButtonAddSchedule;
    RecyclerView rvSchedules;
    ReporteesAdapter adapter;
    List<Reportees> reporteesList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    private String loadedFrom;
    private TextView tvTitle;

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

        tvTitle = view.findViewById(R.id.tv_title);
        floatButtonAddSchedule = view.findViewById(R.id.float_btn_add_schedule);
        rvSchedules = view.findViewById(R.id.rv_schedules_admin);
        layoutManager = new LinearLayoutManager(thisActivity, LinearLayoutManager.VERTICAL, false);
        Bundle extras = getArguments();

        if (extras != null) {
            if (extras.containsKey("from")) {
                loadedFrom = extras.getString("from");

                if (loadedFrom.equalsIgnoreCase("LOCATE_ME")) {
                    tvTitle.setText("Locate Team");
                } else{
                    tvTitle.setText("Employees");
                }
            }
        }
        getEmployeeList();

        floatButtonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFragmentInteraction("ADD_SCHEDULE");
                }
            }
        });

    }

    private void getEmployeeList() {

        if (mListener != null) {
            mListener.showLoading();
        }
        new GetReporteesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Reportees>() {
            @Override
            public void onTaskCompleted(List<Reportees> list) {
                reporteesList = list;
                adapter = new ReporteesAdapter(reporteesList, AdminFragment.this, loadedFrom);
                rvSchedules.setAdapter(adapter);
                rvSchedules.setLayoutManager(layoutManager);

                if (mListener != null) {
                    mListener.hideLoading();
                }

            }

            @Override
            public void onError(String errorMessage) {

                if (mListener != null) {
                    mListener.hideLoading();
                }

            }
        }, Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_REF_ID)).execute();

    }

    @Override
    public void onItemSelected(int position, String action) {
        if (mListener != null) {
            if (action.equalsIgnoreCase("HOME")) {
                mListener.onFragmentInteraction("SALES");
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selected_item", reporteesList.get(position));
                mListener.onFragmentInteraction("LOCATE_ME", bundle);
            }
        }
    }
}

