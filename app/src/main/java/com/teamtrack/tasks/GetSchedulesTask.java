package com.teamtrack.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.teamtrack.database.tables.Schedule;
import com.teamtrack.listeners.OnTaskCompletionListener;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetSchedulesTask extends AsyncTask<String, Integer, String> {

    private List<Schedule> scheduleList;
    private WeakReference<Context> context;
    private OnTaskCompletionListener<Schedule> listener;

    public GetSchedulesTask(Context context, OnTaskCompletionListener<Schedule> listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... param) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onTaskCompleted(scheduleList);
        }
    }
}
