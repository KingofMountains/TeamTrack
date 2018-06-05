package com.teamtrack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamtrack.Utilities.Preferences;
import com.teamtrack.fragments.AddScheduleFragment;
import com.teamtrack.fragments.AdminFragment;
import com.teamtrack.fragments.LocateTeamFragment;
import com.teamtrack.fragments.SalesFragment;
import com.teamtrack.fragments.ScheduleDetailFragment;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.services.ReminderService;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String REMINDER_JOB_TAG = "LOCATION_UPDATE_JOB";
    private static final int REQUEST_CHECK_SETTINGS = 101;

    final int periodicity = (int) TimeUnit.HOURS.toSeconds(1); // Every 1 hour periodicity expressed as seconds
    final int toleranceInterval = (int) TimeUnit.MINUTES.toSeconds(15); // a small(ish) window of time when triggering is OK


    ProgressDialog dialog;
    private DrawerLayout drawerLayout;
    LocationManager locationManager;
    Menu menu;
    NavigationView navigationView;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialize() {

        setUpGoogleClient();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Team Track");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
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

        checkGpsStatus();

        if (!Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE).equalsIgnoreCase("MANAGER")) {
            scheduleLocationUpdateJob();
        }

    }

    private void setUpGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }


    private void scheduleLocationUpdateJob() {

        int SYNC_FLEXTIME_SECONDS = (int) TimeUnit.HOURS.toSeconds(1);

        Driver driver = new GooglePlayDriver(this);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);

//        Intent intent = new Intent(getApplicationContext(), LocationService.class);
//        intent.putExtra("reference_id", Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_REF_ID));
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000 * 60 * 60, pendingIntent);
//        }
    }


    public void checkGpsStatus() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = false;
        if (locationManager != null) {
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        if (GpsStatus || Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE).equalsIgnoreCase("MANAGER")) {
            loadUserHome();
        } else {
            requestLocationSettings();
        }

    }

    private void requestLocationSettings() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }

    private void showGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn On GPS")
                .setMessage("You have to turn on GPS to access the application!")
                .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
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
                loadAddScheduleFragment(null);
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
        this.menu = menu;
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
                navigationView.getMenu().getItem(1).setChecked(true);
                loadAddScheduleFragment(null);
                break;
            case "SALES":
                loadSalesFragment(args[0]);
                break;
            case "ON_MEETING_CREATE":
                navigationView.getMenu().getItem(0).setChecked(true);
                loadUserHome();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String action, Bundle bundle) {
        switch (action) {
            case "LOCATE_ME":
                loadLocateTeamFragment(bundle);
                break;
            case "SCHEDULE_LIST_SELECT":
                loadScheduleDetailsFragment(bundle);
                break;
            case "UPDATE_MEETING":
                loadAddScheduleFragment(bundle);
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

    @Override
    public void hideSideMenu(boolean status) {
        if (status) {
            if (menu != null) {
                menu.findItem(R.id.action_menu).setVisible(false);
            }
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            if (menu != null) {
                menu.findItem(R.id.action_menu).setVisible(true);
            }
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void loadAddScheduleFragment(Bundle bundle) {

        AddScheduleFragment fragment = AddScheduleFragment.newInstance();
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, fragment, "AddScheduleFragment")
                .commit();
    }

    private void loadScheduleDetailsFragment(Bundle bundle) {
        ScheduleDetailFragment fragment = ScheduleDetailFragment.newInstance();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, 0)
                .add(R.id.fragment_container, fragment, "ScheduleDetailFragment")
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
                    .replace(R.id.fragment_container, SalesFragment.newInstance(refID), "SalesFragment")
                    .addToBackStack("SalesFragment")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                    .replace(R.id.fragment_container, SalesFragment.newInstance(refID), "SalesFragment")
                    .commitAllowingStateLoss();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        showToastMessage("Location enabled");
                        loadUserHome();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        showToastMessage("Location cancelled by user");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}
