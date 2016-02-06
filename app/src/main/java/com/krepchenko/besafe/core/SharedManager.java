package com.krepchenko.besafe.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ann on 02.02.2016.
 */
public class SharedManager {

    private final static String HINT_VIEWED = "hint_viewed";
    private SharedPreferences sharedPreferences;

    public SharedManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setHintViewed() {
        sharedPreferences.edit().putBoolean(HINT_VIEWED, true).apply();
    }

    public boolean isHintViewed() {
        return sharedPreferences.getBoolean(HINT_VIEWED, false);
    }
}
