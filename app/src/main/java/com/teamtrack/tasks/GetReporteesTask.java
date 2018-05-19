package com.teamtrack.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Reportees;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetReporteesTask extends AsyncTask<String, Integer, String> {

    private List<Reportees> reporteesList;
    private WeakReference<Context> context;
    private OnTaskCompletionListener<Reportees> listener;

    public GetReporteesTask(Context context, OnTaskCompletionListener<Reportees> listener) {
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
            listener.onTaskCompleted(reporteesList);
        }
    }
}
