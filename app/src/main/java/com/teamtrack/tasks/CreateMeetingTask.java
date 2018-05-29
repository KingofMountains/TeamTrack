package com.teamtrack.tasks;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.network.VolleySingleton;
import com.network.requests.GSONRequest;
import com.teamtrack.BuildConfig;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Meetings;

import java.lang.ref.WeakReference;

public class CreateMeetingTask {

    private WeakReference<Context> context;
    private OnTaskCompletionListener<Meetings> listener;
    private String[] params;

    public CreateMeetingTask(Context context, OnTaskCompletionListener<Meetings> listener, String... params) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.params = params;
    }

    public void execute() {
        String mParams;
        if (params.length >= 7) {
            mParams = "custid=" + params[0] + "&employeeid=" + params[1] + "&customerlocation=" + params[2] + "&scheduledate="
                    + params[3] + "&meetingfrom=" + params[4] + "&meetingto=" + params[5] + "&description=" + params[6];
        } else {
            throw new ArrayIndexOutOfBoundsException("Params should contain all 7 parameters");
        }

        mParams = mParams.replace(" ", "%20");

        if (params.length == 8 && !params[7].equalsIgnoreCase("")) {
            mParams = mParams + "&meeting_id=" + params[7];
        }

        String url = BuildConfig.SERVER_URL + BuildConfig.CREATE_MEETING_URL + mParams;

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

    }
}
