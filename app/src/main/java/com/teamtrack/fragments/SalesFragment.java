package com.teamtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamtrack.R;
import com.teamtrack.database.tables.Meetings;
import com.teamtrack.listeners.OnFragmentInteractionListener;
import com.teamtrack.listeners.OnTaskCompletionListener;
import com.teamtrack.tasks.GetSchedulesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class SalesFragment extends Fragment {

    View view;
    OnFragmentInteractionListener mListener;
    Activity thisActivity;
    List<Meetings> scheduleList = new ArrayList<>();
    TabLayout tabLayout;
    ViewPager viewPager;

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment newInstance(String refID) {
        SalesFragment fragment = new SalesFragment();
        Bundle args = new Bundle();
        args.putString("ref_id", refID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_sales, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
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
        init();
    }

    private void init() {
        tabLayout = view.findViewById(R.id.tab_layout_sales);
        viewPager = view.findViewById(R.id.view_pager_sales);
        getMeetings();
    }

    private void getMeetings() {
        new GetSchedulesTask(thisActivity.getApplicationContext(), new OnTaskCompletionListener<Meetings>() {
            @Override
            public void onTaskCompleted(List<Meetings> list) {
                scheduleList = list;
                viewPager.setAdapter(new TaskPagerAdapter(getChildFragmentManager(), 3));
                tabLayout.setupWithViewPager(viewPager);
                viewPager.setOffscreenPageLimit(3);
                viewPager.setCurrentItem(1);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).execute("");
    }

    private class TaskPagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        TaskPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return SalesMeetingsFragment.newInstance(position, (ArrayList<Meetings>) scheduleList);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Past";
            } else if (position == 1) {
                return "Today";
            } else if (position == 2) {
                return "Future";
            } else {
                return "Today";
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}

