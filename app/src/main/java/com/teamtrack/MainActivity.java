package com.teamtrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.teamtrack.fragments.AddScheduleFragment;
import com.teamtrack.fragments.AdminFragment;
import com.teamtrack.fragments.LocateTeamFragment;
import com.teamtrack.fragments.SalesFragment;
import com.teamtrack.fragments.ScheduleDetailFragment;
import com.teamtrack.listeners.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    ProgressDialog dialog;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Team Track");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        loadMenuItemScreen(menuItem);
                        return true;
                    }
                });
        configureProgressLoading();
        loadUserHome();
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
                loadLocateTeamFragment();
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
                            loadAdminFragment();
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            break;
                        case "SALES_PERSON":
                            loadSalesFragment();
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            loadAdminFragment();
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
        if (id == R.id.action_logout) {
            logout();
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
    public void onFragmentInteraction(String action) {
        switch (action) {
            case "ADD_SCHEDULE":
                loadAddScheduleFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String from, Bundle bundle) {
        loadScheduleDetailsFragment(bundle);
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    private void loadAddScheduleFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, 0)
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

    private void loadSalesFragment() {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, SalesFragment.newInstance(), "")
                .commit();
    }

    private void loadAdminFragment() {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, AdminFragment.newInstance(), "")
                .commit();
    }

    private void loadLocateTeamFragment() {
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragment_container, LocateTeamFragment.newInstance(), "")
                .commit();
    }
}
