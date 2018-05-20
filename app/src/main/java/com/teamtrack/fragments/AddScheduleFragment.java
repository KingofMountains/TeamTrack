package com.teamtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teamtrack.R;
import com.teamtrack.database.tables.Meetings;
import com.teamtrack.database.tables.User;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.tasks.GetAllSalesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class AddScheduleFragment extends Fragment {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    List<String> userList = new ArrayList<>();
    Spinner spinnerSales;
    ArrayAdapter salesArrayAdapter;
    String selectedSalesPerson = "";
    Button btnCreateSchedule;
    EditText etCustomerName, etDescription, etLocation, etRadiusLimit, etRemarks;


    public AddScheduleFragment() {
        // Required empty public constructor
    }

    public static AddScheduleFragment newInstance() {
        return new AddScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_schedule, container, false);
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

        spinnerSales = view.findViewById(R.id.spinner_sales);

        new GetAllSalesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<User>() {
            @Override
            public void onTaskCompleted(List<User> list) {

                if (list == null)
                    return;

                for (User user : list) {
                    userList.add(user.getName());
                }
                salesArrayAdapter = new ArrayAdapter<>(thisActivity, android.R.layout.simple_list_item_1, userList);
                salesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSales.setAdapter(salesArrayAdapter);

            }

            @Override
            public void onError(String errorMessage) {

            }
        }).execute("");

        spinnerSales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSalesPerson = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
        etCustomerName = view.findViewById(R.id.et_customer_name);
        etDescription = view.findViewById(R.id.et_description);
        etLocation = view.findViewById(R.id.et_location);
        etRadiusLimit = view.findViewById(R.id.et_radius_limit);
        etRemarks = view.findViewById(R.id.et_remarks);

        btnCreateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateScheduleClicked();
            }
        });

    }

    private void onCreateScheduleClicked() {
        if (etCustomerName.getText().toString().length() > 0
                && etDescription.getText().toString().length() > 0
                && etLocation.getText().toString().length() > 0
                && etRadiusLimit.getText().toString().length() > 0
                && etRemarks.getText().toString().length() > 0
                && selectedSalesPerson.length() > 0) {

            final Meetings schedule = new Meetings();

            schedule.setCustomerName(etCustomerName.getText().toString());
            schedule.setDescription(etDescription.getText().toString());
            schedule.setLocation(etLocation.getText().toString());
            schedule.setRadiusLimit(Integer.parseInt(etRadiusLimit.getText().toString()));
            schedule.setAssignedTo(selectedSalesPerson);
            schedule.setRemarks(etRemarks.getText().toString());
            schedule.setStatus("Open");

            new CreateScheduleTask(schedule).execute("");
        } else {
            Toast.makeText(thisActivity, "Please enter all details!", Toast.LENGTH_SHORT).show();
        }
    }

    private class CreateScheduleTask extends AsyncTask<String, Void, String> {

        Meetings schedule;

        private CreateScheduleTask(Meetings schedule) {
            this.schedule = schedule;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.showLoading();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mListener != null) {
                mListener.hideLoading();
            }
            Toast.makeText(thisActivity, "New Scheduled created successfully!", Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        }
    }

}

