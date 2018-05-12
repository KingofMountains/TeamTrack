package com.teamtrack.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.teamtrack.database.tables.User;
import com.teamtrack.listeners.OnTaskCompletionListener;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetAllSalesTask extends AsyncTask<String, Integer, String> {

    private List<User> userList;
    private WeakReference<Context> context;
    private OnTaskCompletionListener<User> listener;

    public GetAllSalesTask(Context context, OnTaskCompletionListener<User> listener) {
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
            listener.onTaskCompleted(userList);
        }
    }
}
