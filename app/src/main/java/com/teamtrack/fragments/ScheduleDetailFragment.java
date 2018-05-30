package com.teamtrack.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.teamtrack.R;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Meetings;
import com.teamtrack.tasks.UpdateMeetingSalesTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleDetailFragment extends Fragment {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    TextView tvCustomerName, tvDescription, tvLocation;
    EditText etRemarks;
    Button btnCheckIn;
    Meetings data;
    ArrayAdapter adapter;
    Spinner spinnerMeetingStatus;
    private String mMeetingStatus = "", mStatusUpdatedFrom = "", mStatusUpdatedOn = "", mMeetingUpdates = "", mMeetingId = "";


    public ScheduleDetailFragment() {
        // Required empty public constructor
    }

    public static ScheduleDetailFragment newInstance() {
        return new ScheduleDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_schedule_details, container, false);
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

        Bundle extras = getArguments();

        if (mListener != null) {
            mListener.hideSideMenu(true);
        }

        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvLocation = view.findViewById(R.id.tv_location);
        tvDescription = view.findViewById(R.id.tv_description);
        etRemarks = view.findViewById(R.id.tv_remarks);
        spinnerMeetingStatus = view.findViewById(R.id.spinner_status);
        btnCheckIn = view.findViewById(R.id.btn_check_in);

        configureStatusSpinner();

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canProceedToUpdate();
            }
        });

        if (extras != null && extras.containsKey("selected_item")) {
            data = extras.getParcelable("selected_item");
            if (data != null) {
                tvCustomerName.setText(data.getCustomerName());
                tvLocation.setText(data.getCustomerLocationName());
                tvDescription.setText(data.getDescription());
                mMeetingId = data.getMeetingID();

                if (data.getMeetingStatus().equalsIgnoreCase("Meeting Completed") ||
                        data.getMeetingStatus().equalsIgnoreCase("Meeting Cancelled")) {
                    btnCheckIn.setEnabled(false);
                    btnCheckIn.setAlpha(0.5f);
                    spinnerMeetingStatus.setSelection(data.getMeetingStatus().equalsIgnoreCase("Meeting Completed") ? 1 : 2);
                    spinnerMeetingStatus.setEnabled(false);
                    spinnerMeetingStatus.setAlpha(0.5f);
                }

            }
        }
    }

    private void canProceedToUpdate() {

        mMeetingUpdates = etRemarks.getText().toString();

        if (mMeetingUpdates.equalsIgnoreCase("")) {
            showErrorToast("Please enter meeting updates!");
            return;
        } else if (mMeetingStatus.equalsIgnoreCase("1")) {
            showErrorToast("Please change meeting status!");
            return;
        }

        fetchCurrentLocation();
    }

    private void showErrorToast(String message) {
        Toast.makeText(thisActivity, message, Toast.LENGTH_LONG).show();
    }


    public void configureStatusSpinner() {

        adapter = ArrayAdapter.createFromResource(thisActivity, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeetingStatus.setAdapter(adapter);
        spinnerMeetingStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mMeetingStatus = "" + (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void fetchCurrentLocation() {

        if (mListener != null) {
            mListener.showLoading();
        }

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(thisActivity);

        if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(thisActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            new UpdateScheduleTask(location).execute("");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(thisActivity, "Couldn't get your location. Please make sure GPS is enabled and try again",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private class UpdateScheduleTask extends AsyncTask<String, Void, String> {

        Location location;

        public UpdateScheduleTask(Location location) {
            this.location = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String returnStatus = null;

            try {
                Geocoder geocoder = new Geocoder(thisActivity);
                List<Address> addresses;
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Address currentAddress;
                if (addresses != null) {
                    currentAddress = addresses.get(0);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a", Locale.US);
                    mStatusUpdatedOn = df.format(Calendar.getInstance().getTime());
                    mStatusUpdatedFrom = currentAddress.getSubLocality() + "," + currentAddress.getLocality();
                    returnStatus = "success";
                } else {
                    returnStatus = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateMeetingTask();
        }
    }

    private void updateMeetingTask() {

        if (mStatusUpdatedFrom.equalsIgnoreCase("")) {
            showErrorToast("Couldn't get your location. Please try again!");

            if (mListener != null) {
                mListener.hideLoading();
            }
            return;
        } else if (mStatusUpdatedOn.equalsIgnoreCase("")) {
            showErrorToast("Couldn't get your time. Please try again!");

            if (mListener != null) {
                mListener.hideLoading();
            }
            return;
        }

        String[] params = {mMeetingId, mStatusUpdatedFrom, mStatusUpdatedOn, mMeetingUpdates, mMeetingStatus.trim()};

        new UpdateMeetingSalesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Meetings>() {
            @Override
            public void onTaskCompleted(List<Meetings> list) {

                Toast.makeText(thisActivity, "Meetings updated successfully!", Toast.LENGTH_SHORT).show();

                if (mListener != null) {
                    mListener.hideLoading();
                }
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (mListener != null) {
                    mListener.hideLoading();
                }
            }
        }, params).execute();
    }
}

