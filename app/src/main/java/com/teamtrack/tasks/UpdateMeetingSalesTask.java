package com.teamtrack.tasks;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.network.VolleySingleton;
import com.network.requests.GSONRequest;
import com.teamtrack.BuildConfig;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Meetings;

import java.lang.ref.WeakReference;

public class UpdateMeetingSalesTask {

    private WeakReference<Context> context;
    private OnTaskCompletionListener<Meetings> listener;
    private String[] params;

    public UpdateMeetingSalesTask(Context context, OnTaskCompletionListener<Meetings> listener, String... params) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.params = params;
    }

    public void execute() {
        String mParams;
        if (params.length >= 5) {
            mParams = "meetid=" + params[0] + "&status_from=" + params[1] + "&updated_on=" + params[2] + "&meeting_update=" + params[3]
                    + "&status=" + params[4];
        } else {
            throw new ArrayIndexOutOfBoundsException("Params should contain all 5 parameters");
        }

        mParams = mParams.replace(" ", "%20");

        String url = BuildConfig.SERVER_URL + BuildConfig.UPDATE_MEETING_URL + mParams;

        GSONRequest request = new GSONRequest(Request.Method.GET, url, Meetings.class, null,
                new Response.Listener<Meetings>() {
                    @Override
                    public void onResponse(Meetings response) {
                        Log.d("onResponse :", response.toString());

                        if (listener != null) {
                            listener.onTaskCompleted(null);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponse :" + error);

                if (listener != null) {
                    listener.onError(error.getMessage());
                }
            }
        });

        VolleySingleton.getInstance(context.get()).getRequestQueue().add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
