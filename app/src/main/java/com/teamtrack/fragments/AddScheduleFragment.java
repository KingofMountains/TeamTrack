package com.teamtrack.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.teamtrack.R;
import com.teamtrack.Utilities.Preferences;
import com.teamtrack.listeners.OnDialogItemSelectedListener;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.model.Customer;
import com.teamtrack.model.CustomerLocation;
import com.teamtrack.model.Meetings;
import com.teamtrack.model.Reportees;
import com.teamtrack.tasks.CreateMeetingTask;
import com.teamtrack.tasks.GetCustomersTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class AddScheduleFragment<T> extends Fragment implements OnDialogItemSelectedListener<T> {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    Button btnCreateSchedule;
    Calendar myCalendar;
    String mCustomerID = "", mCustomerName = "", mCustomerLocationID = "", mCustomerLocation = "", mSalesID = "", mSalesName = "",
            mMeetingDate = "", mMeetingFromTime = "", mMeetingToTime = "", mMeetingStatus = "", mDesctiption = "";
    ArrayAdapter adapter;
    Spinner spinnerMeetingStatus;
    TextView tvCustomerName, tvCustomerLocation, tvSalesPerson, tvMeetingDate, tvMeetingFrom, tvMeetingTo;
    EditText txtMeetingDescription;
    RelativeLayout layoutCustomerName, layoutCustomerLocation, layoutSalesPerson;
    ArrayList<Reportees> reporteesList;
    ArrayList<Customer> customerList;
    private boolean todaySelected = false;

    public AddScheduleFragment() {
        // Required empty public constructor
    }

    public static AddScheduleFragment newInstance() {
        return new AddScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_schedule, container, false);
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

    private void init() {

        myCalendar = Calendar.getInstance();
        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvCustomerLocation = view.findViewById(R.id.tv_customer_location);
        tvSalesPerson = view.findViewById(R.id.tv_sales_person);
        tvMeetingDate = view.findViewById(R.id.tv_meeting_date);
        tvMeetingFrom = view.findViewById(R.id.tv_meeting_from_time);
        tvMeetingTo = view.findViewById(R.id.tv_meeting_to_time);
        txtMeetingDescription = view.findViewById(R.id.et_description);
        spinnerMeetingStatus = view.findViewById(R.id.spinner_status);
        layoutCustomerName = view.findViewById(R.id.relative_customer_name);
        layoutCustomerLocation = view.findViewById(R.id.relative_customer_location);
        layoutSalesPerson = view.findViewById(R.id.relative_sales_person);

        configureStatusSpinner();

        getCustomerList();

        btnCreateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateScheduleClicked();
            }
        });

        tvMeetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(thisActivity, date, myCalendar.get(Calendar.YEAR), myCalendar
                        .get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        tvMeetingFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog("from_time");
            }
        });
        tvMeetingTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog("to_time");
            }
        });

        layoutCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectFragment fragment = new SelectFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("select_list", customerList);
                fragment.setArguments(bundle);
                fragment.setCancelable(false);
                fragment.show(getChildFragmentManager(), "SelectSalesFragment");

            }
        });
        layoutCustomerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectFragment fragment = new SelectFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("select_list", customerList.get(0).getLocationList());
                fragment.setArguments(bundle);
                fragment.setCancelable(false);
                fragment.show(getChildFragmentManager(), "SelectSalesFragment");
            }
        });
        layoutSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reporteesList = (ArrayList<Reportees>) Preferences.sharedInstance().getReporteeResponse().getReportingList();
                SelectFragment fragment = new SelectFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("select_list", reporteesList);
                fragment.setArguments(bundle);
                fragment.setCancelable(false);
                fragment.show(getChildFragmentManager(), "SelectSalesFragment");
            }
        });

    }

    private void getCustomerList() {

        if (mListener != null) {
            mListener.showLoading();
        }

        new GetCustomersTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Customer>() {
            @Override
            public void onTaskCompleted(List<Customer> list) {
                customerList = (ArrayList<Customer>) list;
                if (mListener != null) {
                    mListener.hideLoading();
                }
            }

            @Override
            public void onError(String errorMessage) {

                if (mListener != null) {
                    mListener.hideLoading();
                }
            }
        }, Preferences.sharedInstance().getString(Preferences.Key.EMPLOYEE_REF_ID)).execute();

    }

    private void showTimePickerDialog(final String which) {

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(thisActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);
                if (datetime.getTimeInMillis() >= c.getTimeInMillis() || !todaySelected) {
                    selectedHour %= 12;
                    String selectedTime = String.format(Locale.US, "%02d:%02d %s", selectedHour == 0 ? 12 : selectedHour,
                            selectedMinute, selectedHour < 12 ? "AM" : "PM");
                    if (which.equalsIgnoreCase("from_time")) {
                        tvMeetingFrom.setText(selectedTime);
                        mMeetingFromTime = selectedTime;
                        tvMeetingFrom.setAlpha(1f);
                    } else {
                        tvMeetingTo.setText(selectedTime);
                        mMeetingToTime = selectedTime;
                        tvMeetingTo.setAlpha(1f);
                    }
                } else {
                    showErrorToast("Invalid time. Cannot select past time as date selected is today.");
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd MMM yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvMeetingDate.setText(sdf.format(myCalendar.getTime()));
        mMeetingDate = tvMeetingDate.getText().toString();
        tvMeetingDate.setAlpha(1f);
        try {
            Date selectedDate = sdf.parse(mMeetingDate);
            Date currentDate = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
            todaySelected = currentDate.equals(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void onCreateScheduleClicked() {

        mDesctiption = txtMeetingDescription.getText().toString();

        if (mCustomerName.equalsIgnoreCase("")) {
            showErrorToast("Please select Customer!");
            return;
        } else if (mCustomerLocation.equalsIgnoreCase("")) {
            showErrorToast("Please select Customer Location!");
            return;
        } else if (mSalesName.equalsIgnoreCase("")) {
            showErrorToast("Please select Sales Person!");
            return;
        } else if (mMeetingDate.equalsIgnoreCase("")) {
            showErrorToast("Please select Date!");
            return;
        } else if (mMeetingFromTime.equalsIgnoreCase("")) {
            showErrorToast("Please select From Time!");
            return;
        } else if (mMeetingToTime.equalsIgnoreCase("")) {
            showErrorToast("Please select To Time!");
            return;
        } else if (!mMeetingStatus.equalsIgnoreCase("New Appointment")) {
            showErrorToast("Meeting status should be New Appointment!");
            return;
        } else if (mDesctiption.equalsIgnoreCase("")) {
            showErrorToast("Please enter description!");
            return;
        }

        if (mListener != null) {
            mListener.showLoading();
        }

        String[] params = {mCustomerID, mSalesID, mCustomerLocationID, mMeetingDate, mMeetingFromTime, mMeetingToTime, mDesctiption};
        try {
            new CreateMeetingTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Meetings>() {
                @Override
                public void onTaskCompleted(List<Meetings> list) {

                    showErrorToast("Meeting created successfully!");

                    if (mListener != null) {
                        mListener.hideLoading();
                    }

                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }

                @Override
                public void onError(String errorMessage) {

                    if (mListener != null) {
                        mListener.hideLoading();
                    }
                }
            }, params).execute();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void configureStatusSpinner() {

        adapter = ArrayAdapter.createFromResource(thisActivity, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeetingStatus.setAdapter(adapter);
        spinnerMeetingStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mMeetingStatus = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void showErrorToast(String message) {
        Toast.makeText(thisActivity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(T data) {

        if (data instanceof Reportees) {
            mSalesID = ((Reportees) data).getId();
            mSalesName = ((Reportees) data).getName();
            tvSalesPerson.setText(mSalesName);
        } else if (data instanceof Customer) {
            mCustomerID = ((Customer) data).getId();
            mCustomerName = ((Customer) data).getName();
            tvCustomerName.setText(mCustomerName);
        } else if (data instanceof CustomerLocation) {
            mCustomerLocationID = ((CustomerLocation) data).getId();
            mCustomerLocation = ((CustomerLocation) data).getName();
            tvCustomerLocation.setText(mCustomerLocation);
        }
    }
}

