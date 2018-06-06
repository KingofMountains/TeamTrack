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
            mMeetingDate = "", mMeetingFromTime = "", mMeetingToTime = "", mMeetingStatus = "", mDescription = "", mMeetingID = "";
    ArrayAdapter adapter;
    Spinner spinnerMeetingStatus;
    TextView tvCustomerName, tvCustomerLocation, tvSalesPerson, tvMeetingDate, tvMeetingFrom, tvMeetingTo, tvMeetingUpdatedFrom,
            tvMeetingUpdatedOn, tvMeetingUpdates;
    EditText txtMeetingDescription;
    RelativeLayout layoutCustomerName, layoutCustomerLocation, layoutSalesPerson, layoutMeetingUpdatedFrom, layoutMeetingUpdatedOn,
            layoutMeetingUpdates;
    ArrayList<Reportees> reporteesList;
    ArrayList<Customer> customerList;
    ArrayList<CustomerLocation> customerLocationsList;
    private boolean todaySelected = false;
    Meetings data;
    private boolean isUpdate = false;

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

        Bundle extras = getArguments();
        if (extras != null) {
            if (extras.containsKey("selected_item")) {
                data = extras.getParcelable("selected_item");
                if (data != null) {
                    isUpdate = true;
                }
            }
        }

        myCalendar = Calendar.getInstance();
        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvCustomerLocation = view.findViewById(R.id.tv_customer_location);
        tvSalesPerson = view.findViewById(R.id.tv_sales_person);
        tvMeetingDate = view.findViewById(R.id.tv_meeting_date);
        tvMeetingFrom = view.findViewById(R.id.tv_meeting_from_time);
        tvMeetingTo = view.findViewById(R.id.tv_meeting_to_time);
        tvMeetingUpdatedFrom = view.findViewById(R.id.et_status_from);
        tvMeetingUpdatedOn = view.findViewById(R.id.et_status_on);
        tvMeetingUpdates = view.findViewById(R.id.et_meeting_update);
        txtMeetingDescription = view.findViewById(R.id.et_description);
        spinnerMeetingStatus = view.findViewById(R.id.spinner_status);
        layoutCustomerName = view.findViewById(R.id.relative_customer_name);
        layoutCustomerLocation = view.findViewById(R.id.relative_customer_location);
        layoutSalesPerson = view.findViewById(R.id.relative_sales_person);
        layoutMeetingUpdatedFrom = view.findViewById(R.id.relative_status_from);
        layoutMeetingUpdatedOn = view.findViewById(R.id.relative_status_on);
        layoutMeetingUpdates = view.findViewById(R.id.relative_meeting_update);

        reporteesList = (ArrayList<Reportees>) Preferences.sharedInstance().getReporteeResponse().getReportingList();

        configureStatusSpinner();

        if (isUpdate) {
            setupUpdateDetails(data);
        } else {
            getCustomerList();
        }


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
                if (mMeetingDate.equalsIgnoreCase("")) {
                    showErrorToast("Select meeting date first!");
                    return;
                }
                showTimePickerDialog("from_time");
            }
        });
        tvMeetingTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMeetingDate.equalsIgnoreCase("")) {
                    showErrorToast("Select meeting date first!");
                    return;
                } else if (mMeetingFromTime.equalsIgnoreCase("")) {
                    showErrorToast("Select from time first!");
                    return;
                }
                showTimePickerDialog("to_time");
            }
        });

        layoutCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customerList != null && customerList.size() == 0) {
                    showErrorToast("No Customer Found. Please create a customer!");
                    return;
                }

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
                if (customerList != null && mCustomerID.equalsIgnoreCase("")) {
                    showErrorToast("Please select Customer!");
                    return;
                }

                customerLocationsList = getCustomerLocationList(mCustomerID);

                if (customerLocationsList != null && customerLocationsList.size() == 0) {
                    showErrorToast("No locations found for selected customer. Please add location to customer!");
                    return;
                }

                if (customerList != null && customerList.size() > 0) {
                    SelectFragment fragment = new SelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("select_list", customerLocationsList);
                    fragment.setArguments(bundle);
                    fragment.setCancelable(false);
                    fragment.show(getChildFragmentManager(), "SelectSalesFragment");
                }
            }
        });
        layoutSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reporteesList != null && reporteesList.size() == 0) {
                    showErrorToast("No employees found. Please create employees!");
                    return;
                }
                SelectFragment fragment = new SelectFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("select_list", reporteesList);
                fragment.setArguments(bundle);
                fragment.setCancelable(false);
                fragment.show(getChildFragmentManager(), "SelectSalesFragment");
            }
        });

    }

    private ArrayList<CustomerLocation> getCustomerLocationList(String mCustomerID) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equalsIgnoreCase(mCustomerID)) {
                return customer.getLocationList();
            }
        }
        return null;
    }

    private void setupUpdateDetails(Meetings data) {

        mSalesName = getEmployeeName(data.getEmployeeId());
        mCustomerName = data.getCustomerName();
        mCustomerLocation = data.getCustomerLocationName();
        mCustomerID = data.getCustomerID();
        mSalesID = data.getEmployeeId();
        mCustomerLocationID = data.getCustomerLocationID();
        mMeetingDate = data.getScheduledDate();
        mMeetingFromTime = data.getMeetingFromTime();
        mMeetingToTime = data.getMeetingToTime();
        mDescription = data.getDescription();
        mMeetingID = data.getMeetingID();

        tvCustomerName.setText(mCustomerName);
        tvCustomerLocation.setText(mCustomerLocation);
        tvSalesPerson.setText(mSalesName);
        tvMeetingDate.setText(mMeetingDate);
        tvMeetingFrom.setText(mMeetingFromTime);
        tvMeetingTo.setText(mMeetingToTime);
        txtMeetingDescription.setText(mDescription);
        btnCreateSchedule.setText("Update");

        if (data.getMeetingStatus().equalsIgnoreCase("New Appointment")) {
            spinnerMeetingStatus.setSelection(0);
        } else if (data.getMeetingStatus().equalsIgnoreCase("Meeting Cancelled")) {
            spinnerMeetingStatus.setSelection(2);
        } else if (data.getMeetingStatus().equalsIgnoreCase("Meeting Completed")) {
            spinnerMeetingStatus.setSelection(1);
        }

        if (data.getMeetingStatus().equalsIgnoreCase("Meeting Completed") ||
                data.getMeetingStatus().equalsIgnoreCase("Meeting Cancelled")) {
            layoutCustomerName.setEnabled(false);
            layoutCustomerLocation.setEnabled(false);
            layoutSalesPerson.setEnabled(false);
            tvMeetingDate.setEnabled(false);
            tvMeetingFrom.setEnabled(false);
            tvMeetingTo.setEnabled(false);
            txtMeetingDescription.setEnabled(false);
            btnCreateSchedule.setEnabled(false);
            tvMeetingUpdatedFrom.setText(data.getStatusUpdatedFrom());
            tvMeetingUpdatedOn.setText(data.getStatusUpdatedOn());
            tvMeetingUpdates.setText(data.getMeetingUpdates());
            layoutMeetingUpdatedFrom.setVisibility(View.VISIBLE);
            layoutMeetingUpdatedOn.setVisibility(View.VISIBLE);
            layoutMeetingUpdates.setVisibility(View.VISIBLE);
            btnCreateSchedule.setAlpha(0.5f);
        } else {
            getCustomerList();
        }

    }

    private String getEmployeeName(String employeeId) {
        String employeeName = "";
        for (Reportees reportees : reporteesList) {
            if (employeeId.equalsIgnoreCase(reportees.getEmpId())) {
                employeeName = reportees.getEmpName();
            }
        }
        return employeeName;
    }

    private void getCustomerList() {

        if (Preferences.sharedInstance().getCustomerResponse() != null) {
            customerList = (ArrayList<Customer>) Preferences.sharedInstance().getCustomerResponse().getCustomerList();
            return;
        }

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
                    if (selectedHour > 12) {
                        selectedHour %= 12;
                    }
                    String selectedTime = String.format(Locale.US, "%02d:%02d %s", selectedHour == 0 ? 12 : selectedHour,
                            selectedMinute, selectedHour < 12 ? "AM" : "PM");
                    if (which.equalsIgnoreCase("from_time")) {
                        tvMeetingFrom.setText(selectedTime);
                        mMeetingFromTime = selectedTime;
                        tvMeetingFrom.setAlpha(1f);
                        clearToTime();
                    } else {
                        tvMeetingTo.setText(selectedTime);
                        mMeetingToTime = selectedTime;
                        tvMeetingTo.setAlpha(1f);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
                        Date fromTime = null, toTime = null;
                        try {
                            fromTime = dateFormat.parse(mMeetingDate + " " + mMeetingFromTime);
                            toTime = dateFormat.parse(mMeetingDate + " " + mMeetingToTime);

                            if (toTime.before(fromTime)) {
                                showErrorToast("To time should be greater than From time!");
                                clearToTime();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    showErrorToast("Invalid time. Cannot select past time as date selected is today.");
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void clearFromTime() {
        tvMeetingFrom.setText("HH:MM AM");
        mMeetingFromTime = "";
        tvMeetingFrom.setAlpha(0.5f);
    }

    private void clearToTime() {
        tvMeetingTo.setText("HH:MM AM");
        mMeetingToTime = "";
        tvMeetingTo.setAlpha(0.5f);
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
        String myFormat = "dd MMM yyyy";
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

        clearFromTime();
        clearToTime();
    }

    private void onCreateScheduleClicked() {

        mDescription = txtMeetingDescription.getText().toString();

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
        } else if (mDescription.equalsIgnoreCase("")) {
            showErrorToast("Please enter description!");
            return;
        }

        if (mListener != null) {
            mListener.showLoading();
        }

        String[] params = {mCustomerID, mSalesID, mCustomerLocationID, mMeetingDate, mMeetingFromTime, mMeetingToTime, mDescription,
                mMeetingID};
        try {
            new CreateMeetingTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Meetings>() {
                @Override
                public void onTaskCompleted(List<Meetings> list) {

                    if (mMeetingID.equalsIgnoreCase("")) {
                        showErrorToast("Meeting created successfully!");
                    } else {
                        showErrorToast("Meeting updated successfully!");
                    }

                    if (mListener != null) {
                        mListener.hideLoading();
                        mListener.onFragmentInteraction("ON_MEETING_CREATE");
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
                spinnerMeetingStatus.setAlpha(0.5f);
                spinnerMeetingStatus.setEnabled(false);
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

