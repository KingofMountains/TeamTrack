package com.teamtrack.services;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.teamtrack.Utilities.Preferences;

public class ReminderService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.i("Location", "onStartJob");

        startService(new Intent(this, LocationService.class).putExtra("reference_id",
                Preferences.sharedInstance(this).getString(Preferences.Key.EMPLOYEE_REF_ID)));

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Job Started", Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Location", "onStopJob");
        /* true means, we're not done, please reschedule */
        return false;
    }
}