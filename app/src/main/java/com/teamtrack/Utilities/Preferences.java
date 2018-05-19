package com.teamtrack.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String SETTINGS_NAME = "default_settings";
    private SharedPreferences sharedPreferences;
    private static Preferences mPreference;
    private SharedPreferences.Editor mEditor;

    public enum Key {
        EMPLOYEE_NAME,
        EMPLOYEE_ID,
        EMPLOYEE_TYPE,
        EMPLOYEE_REF_ID,
    }

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static Preferences sharedInstance(Context context) {
        if (mPreference == null) {
            mPreference = new Preferences(context);
        }
        return mPreference;
    }

    public static Preferences sharedInstance() {
        if (mPreference != null) {
            return mPreference;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The enum of the key(s) to be removed.
     */
    public void remove(Key... keys) {
        edit();
        for (Key key : keys) {
            mEditor.remove(key.name());
        }
        commit();
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        edit();
        mEditor.clear();
        commit();
    }

    public void edit() {
        mEditor = sharedPreferences.edit();
    }

    public void commit() {
        mEditor.commit();
        mEditor = null;
    }

    public void put(Key key, String value) {
        edit();
        mEditor.putString(key.name(), value);
        commit();
    }

    public String getString(Key key) {
        return sharedPreferences.getString(key.name(), null);
    }
}
