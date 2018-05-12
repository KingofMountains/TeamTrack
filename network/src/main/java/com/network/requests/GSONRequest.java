package com.network.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GIRIN on 23-Dec-17.
 */

public class GSONRequest extends Request {

    private Response.Listener listener;
    private Map<String, String> mParams = new HashMap<>();
    private Map<String, String> mHeaders = new HashMap<>();
    private Gson gson;
    private Class responseClass;

    public GSONRequest(int method, String url, Class responseClass, Map<String, String> params, Response.Listener
            listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mParams = params;
        this.listener = listener;
        this.responseClass = responseClass;
        gson = new Gson();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //TODO add headers
        return this.mHeaders;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(gson.fromJson(jsonString, responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }

    @Override
    public int compareTo(Object object) {
        return 0;
    }
}
