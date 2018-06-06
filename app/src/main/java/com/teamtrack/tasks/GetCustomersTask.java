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
import com.teamtrack.Utilities.Preferences;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Customer;
import com.teamtrack.model.response.CustomerListResponse;

import java.lang.ref.WeakReference;

public class GetCustomersTask {

    private WeakReference<Context> context;
    private OnTaskCompletionListener<Customer> listener;
    private String refID;

    public GetCustomersTask(Context context, OnTaskCompletionListener<Customer> listener, String refID) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
        this.refID = refID;
    }

    public void execute() {

        String url = BuildConfig.SERVER_URL + BuildConfig.CUSTOMER_LIST_OTP_URL;

        GSONRequest request = new GSONRequest(Request.Method.GET, url, CustomerListResponse.class, null,
                new Response.Listener<CustomerListResponse>() {
                    @Override
                    public void onResponse(CustomerListResponse response) {
                        Log.d("onResponse :", response.toString());

                        if (response.getCustomerList() != null) {
                            if (listener != null) {
                                listener.onTaskCompleted(response.getCustomerList());
                                Preferences.sharedInstance().put(Preferences.Key.CUSTOMER_LIST, response);
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
