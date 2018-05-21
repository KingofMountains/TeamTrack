package com.teamtrack.listeners;

import android.os.Bundle;

public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String from, String... args);
        void onFragmentInteraction(String from, Bundle bundle);
        void showLoading();
        void hideLoading();
}