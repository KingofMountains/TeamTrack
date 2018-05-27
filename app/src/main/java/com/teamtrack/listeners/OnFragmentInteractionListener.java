package com.teamtrack.listeners;

import android.os.Bundle;

public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String from, String... args);
        void onFragmentInteraction(String from, Bundle bundle);
        void showLoading();
        void hideLoading();
        void hideSideMenu(boolean status);
}