package com.teamtrack.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.teamtrack.R;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.model.Meetings;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleDetailFragment extends Fragment implements LocationListener {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    TextView tvCustomerName, tvDescription, tvLocation, tvStatus;
    EditText etRemarks;
    Button btnCheckIn;
    String streetAddress;
    Meetings data;
    int radiusLimit = 0;
    private FusedLocationProviderClient mFusedLocationClient;


    public ScheduleDetailFragment() {
        // Required empty public constructor
    }

    public static ScheduleDetailFragment newInstance() {
        return new ScheduleDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvLocation = view.findViewById(R.id.tv_location);
        tvDescription = view.findViewById(R.id.tv_description);
        etRemarks = view.findViewById(R.id.tv_remarks);
        tvStatus = view.findViewById(R.id.tv_status);
        btnCheckIn = view.findViewById(R.id.btn_check_in);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCurrentLocation();
            }
        });

        if (extras != null && extras.containsKey("selected_item")) {
            data = extras.getParcelable("selected_item");
            if (data != null) {
                tvCustomerName.setText(data.getCustomerName());
//                tvLocation.setText(data.getLocation());
//                tvDescription.setText(data.getDescription());
//                etRemarks.setText(data.getRemarks());
//                tvStatus.setText(data.getStatus());
//                streetAddress = data.getLocation();
//                radiusLimit = data.getRadiusLimit();
            }
        }
    }

    private void fetchCurrentLocation() {

        if (mListener != null) {
            mListener.showLoading();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(thisActivity);

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

    /**
     * calculates the distance between two locations in MILES(meters)
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist * 1609.344; // converting miles into meters.
    }

    @Override
    public void onLocationChanged(Location location) {

        new UpdateScheduleTask(location).execute("");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
                    String date = df.format(Calendar.getInstance().getTime());
                    String sLocation = currentAddress.getSubLocality() + "," + currentAddress.getLocality();
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

            if (mListener != null) {
                mListener.hideLoading();
            }
            if (result != null && result.equalsIgnoreCase("success")) {
                Toast.makeText(thisActivity, "Meetings updated successfully!", Toast.LENGTH_SHORT).show();
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            } else {
                Toast.makeText(thisActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

