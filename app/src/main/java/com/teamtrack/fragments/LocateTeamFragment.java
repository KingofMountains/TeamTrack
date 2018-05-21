package com.teamtrack.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamtrack.R;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.model.Reportees;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocateTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocateTeamFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private Reportees data;
    private TextView tvEmployeeName, tvLastUpdated;
    private double latitude, longitude;

    public LocateTeamFragment() {
        // Required empty public constructor
    }

    public static LocateTeamFragment newInstance() {
        return new LocateTeamFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_locate_team, container, false);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

        tvEmployeeName = view.findViewById(R.id.tv_location_team_employee_name);
        tvLastUpdated = view.findViewById(R.id.tv_location_team_employee_last_known_time);
        Bundle extras = getArguments();

        if (extras != null && extras.containsKey("selected_item")) {
            data = extras.getParcelable("selected_item");
            if (data != null) {

                tvEmployeeName.setText(data.getEmpName() != null ? data.getEmpName() : "null");
                tvLastUpdated.setText(data.getLastUpdatedLocation() != null ? data.getLastUpdatedLocation() : "null");

                try {
                    latitude = Double.valueOf(data.getLatitude());
                    longitude = Double.valueOf(data.getLongitude());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    setDefaultLocation();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    setDefaultLocation();
                }
            }
        }
    }

    private void setDefaultLocation() {
        latitude = 11.0168;
        longitude = 76.9558;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng empLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(empLocation).title(data.getEmpName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(empLocation , 12));
    }
}
