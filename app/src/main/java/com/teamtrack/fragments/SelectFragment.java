package com.teamtrack.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamtrack.R;
import com.teamtrack.adapters.SelectAdapter;
import com.teamtrack.listeners.OnDialogItemSelectedListener;
import com.teamtrack.listeners.OnItemSelectedListener;

import java.util.List;

public class SelectFragment<T> extends DialogFragment implements OnItemSelectedListener {
    private View view;
    private Activity thisActivity;
    private List<? extends T> selectList;
    private OnDialogItemSelectedListener<T> mListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_dialog, container, false);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (OnDialogItemSelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement Callback interface");
        }
    }

    private void init() {

        Bundle extras = getArguments();

        if (extras != null) {
            if (extras.containsKey("select_list")) {
                selectList = extras.getParcelableArrayList("select_list");
            }
        }

        if (selectList != null) {
            RecyclerView rvSelect = view.findViewById(R.id.rv_select);
            rvSelect.setLayoutManager(new LinearLayoutManager(thisActivity));
            rvSelect.setAdapter(new SelectAdapter<>(selectList, this));
        } else {
            dismiss();
        }

    }

    @Override
    public void onItemSelected(int position, String action) {
        if (mListener != null) {
            mListener.onItemSelected(selectList.get(position));
            dismiss();
        }
    }
}