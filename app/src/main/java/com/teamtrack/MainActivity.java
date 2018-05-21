package com.teamtrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.teamtrack.Utilities.Preferences;
import com.teamtrack.fragments.AddScheduleFragment;
import com.teamtrack.fragments.AdminFragment;
import com.teamtrack.fragments.LocateTeamFragment;
import com.teamtrack.fragments.SalesFragment;
import com.teamtrack.fragments.ScheduleDetailFragment;
import com.teamtrack.listeners.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    ProgressDialog dialog;
    private DrawerLayout drawerLayout;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Team Track");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        if (!menuItem.isChecked()) {
                            // set item as selected to persist highlight
                            menuItem.setChecked(true);

                            loadMenuItemScreen(menuItem);
                        }
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        configureProgressLoading();

        CheckGpsStatus();
    }

    public void CheckGpsStatus() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = false;
        if (locationManager != null) {
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        if (GpsStatus || Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE).equalsIgnoreCase("MANAGER")) {
            loadUserHome();
        } else {
            showGPSAlert();
        }

    }

    private void showGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn On GPS")
                .setMessage("You have to turn on GPS to access the application!")
                .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void loadMenuItemScreen(MenuItem menuItem) {
        switch (menuItem.getTitle().toString()) {
            case "Home":
                loadUserHome();
                break;
            case "Create Meeting":
                loadAddScheduleFragment();
                break;
            case "Locate Team":
                loadAdminFragment("LOCATE_ME");
                break;
            case "Logout":
                logout();
                break;
            default:
                break;
        }
    }

    private void loadUserHome() {

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Bundle extras = getIntent().getExtras();
        String userType;
        if (extras != null) {
            if (extras.containsKey("user_type")) {
                userType = extras.getString("user_type");
                if (userType != null) {
                    switch (userType) {
                        case "MANAGER":
                            loadAdminFragment("HOME");
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            break;
                        case "SALES_PERSON":
                            loadSalesFragment(Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_REF_ID));
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            loadSalesFragment(Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_REF_ID));
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void configureProgressLoading() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu) {
            drawerLayout.openDrawer(Gravity.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onFragmentInteraction(String action, String... args) {
        switch (action) {
            case "ADD_SCHEDULE":
                loadAddScheduleFragment();
                break;
            case "SALES":
                loadSalesFragment(args[0]);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String action, Bundle bundle) {
        switch (action) {
            case "SALES":
                loadScheduleDetailsFragment(bundle);
                break;
            case "LOCATE_ME":
                loadLocateTeamFragment(bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void loadAddScheduleFragment() {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, AddScheduleFragment.newInstance(), "AddScheduleFragment")
                .addToBackStack("AddScheduleFragment")
                .commit();
    }

    private void loadScheduleDetailsFragment(Bundle bundle) {
        ScheduleDetailFragment fragment = ScheduleDetailFragment.newInstance();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, fragment, "ScheduleDetailFragment")
                .addToBackStack("ScheduleDetailFragment")
                .commit();
    }

    private void loadSalesFragment(String refID) {
        if (refID != null && refID.equalsIgnoreCase("")) {
            return;
        }

        if (Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE).equalsIgnoreCase("MANAGER")) {
            getSupportFragmentManager()
                    .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                    .add(R.id.fragment_container, SalesFragment.newInstance(refID), "SalesFragment")
                    .addToBackStack("SalesFragment")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                    .replace(R.id.fragment_container, SalesFragment.newInstance(refID), "SalesFragment")
                    .commit();
        }
    }

    private void loadAdminFragment(String from) {

        AdminFragment fragment = AdminFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, fragment, "AdminFragment")
                .commit();
    }

    private void loadLocateTeamFragment(final Bundle bundle) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LocateTeamFragment fragment = LocateTeamFragment.newInstance();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                        .add(R.id.fragment_container, fragment, "LocateTeamFragment")
                        .addToBackStack("LocateTeamFragment")
                        .commit();
            }
        }, 100);
    }
}
