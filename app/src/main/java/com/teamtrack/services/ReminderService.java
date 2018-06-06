package com.teamtrack.services;

import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.teamtrack.Utilities.Preferences;

public class ReminderService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.i("Location", "onStartJob");
        startService(new Intent(this, LocationService.class).putExtra("reference_id",
                Preferences.sharedInstance(this).getString(Preferences.Key.EMPLOYEE_REF_ID)));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Location", "onStopJob");
        /* true means, we're not done, please reschedule */
        return false;
    }
}