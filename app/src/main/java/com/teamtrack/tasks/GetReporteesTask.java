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
import com.teamtrack.model.Reportees;
import com.teamtrack.model.response.ReporteeListResponse;

import java.lang.ref.WeakReference;

public class GetReporteesTask {

    private WeakReference<Context> context;
    private OnTaskCompletionListener<Reportees> listener;
    private String refID;

    public GetReporteesTask(Context context, OnTaskCompletionListener<Reportees> listener, String refID) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.refID = refID;
    }

    public void execute() {

        String url = BuildConfig.SERVER_URL + BuildConfig.REPORTEE_LIST_OTP_URL + "refid=" + refID;

        GSONRequest request = new GSONRequest(Request.Method.GET, url, ReporteeListResponse.class, null,
                new Response.Listener<ReporteeListResponse>() {
                    @Override
                    public void onResponse(ReporteeListResponse response) {
                        Log.d("onResponse :", response.getReportingList().toString());

                        if (response.getReportingList() != null) {
                            if (listener != null) {
                                listener.onTaskCompleted(response.getReportingList());
                            }
                        } else {
                            if (listener != null) {
                                listener.onError("No Reportees Found!");
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponse :"+ error);

                if (listener != null) {
                    listener.onError(error.getMessage());
                }
            }
        });

        VolleySingleton.getInstance(context.get()).getRequestQueue().add(request);

    }
}
