package com.teamtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.network.VolleySingleton;
import com.network.requests.GSONRequest;
import com.teamtrack.BuildConfig;
import com.teamtrack.R;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.model.response.RegisterUserResponse;
import com.teamtrack.model.response.ValidateOtpResponse;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText txtEmpCode, txtMobileNo, txtOTP;
    private LinearLayout layoutEmployeeCodePassword, layoutOTP;
    private Button btnNext;
    private View view;
    private OnFragmentInteractionListener mListener;
    private Activity thisActivity;
    private String userType;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_login, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
        init();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void init() {
        txtEmpCode = view.findViewById(R.id.login_txt_employee_code);
        txtMobileNo = view.findViewById(R.id.login_txt_mobile_no);
        txtOTP = view.findViewById(R.id.login_txt_otp);
        btnNext = view.findViewById(R.id.login_btn_next);
        layoutEmployeeCodePassword = view.findViewById(R.id.layout_employee_code_password);
        layoutOTP = view.findViewById(R.id.layout_otp);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnNext.getText().toString().equalsIgnoreCase("Continue")) {
                    validateOTP(txtMobileNo.getText().toString(), txtOTP.getText().toString());
                } else {
                    validateUserCredentials(txtEmpCode.getText().toString(), txtMobileNo.getText().toString());
                }
            }
        });
    }

    private void validateUserCredentials(final String empCode, final String mobileNo) {

        if (empCode.length() == 0 || mobileNo.length() == 0) {
            Toast.makeText(thisActivity, "Please enter username and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mListener != null) {
            mListener.showLoading();
        }

        Map<String, String> params = new HashMap<>();
        params.put("emp_code", empCode);
        params.put("mobile", mobileNo);

        GSONRequest request = new GSONRequest(Request.Method.POST, BuildConfig.SERVER_URL, RegisterUserResponse.class, params,
                new Response.Listener<RegisterUserResponse>() {
                    @Override
                    public void onResponse(RegisterUserResponse response) {
                        btnNext.setText("Continue");
                        loadOTPView();
                        if (mListener != null) {
                            mListener.hideLoading();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (mListener != null) {
                    mListener.hideLoading();
                }
            }
        });

        VolleySingleton.getInstance(thisActivity).getRequestQueue().add(request);

    }

    private void validateOTP(String mobile, String otp) {

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("otp", otp);

        GSONRequest request = new GSONRequest(Request.Method.POST, BuildConfig.SERVER_URL, ValidateOtpResponse.class, params,
                new Response.Listener<ValidateOtpResponse>() {
                    @Override
                    public void onResponse(ValidateOtpResponse response) {

//                        Preferences.sharedInstance().put(Preferences.Key.EMPLOYEE_ID, "");

                        loadUserHome();
                        if (mListener != null) {
                            mListener.hideLoading();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (mListener != null) {
                    mListener.hideLoading();
                }
            }
        });

        VolleySingleton.getInstance(thisActivity).getRequestQueue().add(request);

    }

    private void loadOTPView() {
        layoutEmployeeCodePassword.setVisibility(View.GONE);
        layoutOTP.setVisibility(View.VISIBLE);
    }

    private void loadUserHome() {
        if (mListener != null) {
            mListener.onFragmentInteraction(userType);
        }
    }
}

