package com.teamtrack;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.teamtrack.Utilities.Preferences;
import com.teamtrack.fragments.LoginFragment;
import com.teamtrack.listeners.OnFragmentInteractionListener;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initializing Preferences
        Preferences.sharedInstance(getApplicationContext());

        configureProgressLoading();

        SplashActivityPermissionsDispatcher.loadLoginFragmentWithPermissionCheck(SplashActivity.this);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void loadLoginFragment() {

        if (Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_ID) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, 0)
                    .replace(R.id.login_fragment_container, LoginFragment.newInstance(), "LoginFragment")
                    .commit();
        } else {
            loadMainActivity(Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_TYPE));
        }
    }

    private void configureProgressLoading() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void exitApplication() {
        finish();
    }

    @Override
    public void onFragmentInteraction(String userType,String... args) {
        loadMainActivity(userType);
    }

    @Override
    public void onFragmentInteraction(String from, Bundle bundle) {
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    private void loadMainActivity(String userType) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("user_type", userType));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
