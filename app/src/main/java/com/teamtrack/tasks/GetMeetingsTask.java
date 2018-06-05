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
import com.teamtrack.model.response.MeetingListResponse;

import java.lang.ref.WeakReference;

public class GetMeetingsTask {

    private WeakReference<Context> context;
    private OnTaskCompletionListener<Meetings> listener;
    private String refID;

    public GetMeetingsTask(Context context, OnTaskCompletionListener<Meetings> listener, String refID) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.refID = refID;
    }

    public void execute() {

        String url = BuildConfig.SERVER_URL + BuildConfig.MEETING_LIST_URL + "refid=" + refID;

        GSONRequest request = new GSONRequest(Request.Method.GET, url, MeetingListResponse.class, null,
                new Response.Listener<MeetingListResponse>() {
                    @Override
                    public void onResponse(MeetingListResponse response) {
                        Log.d("onResponse :", response.getMeetingsList().toString());

                        if (response.getMeetingsList() != null) {
                            if (listener != null) {
                                listener.onTaskCompleted(response.getMeetingsList());
                            }
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
