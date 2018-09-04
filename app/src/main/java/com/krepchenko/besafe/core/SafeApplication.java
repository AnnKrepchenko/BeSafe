package com.krepchenko.besafe.core;

import android.app.Application;

/**
 * Created by Ann on 19.10.2015.
 */
public class SafeApplication extends Application {

    private SharedManager sharedManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedManager = new SharedManager(this);
    }

    public SharedManager getSharedManager() {
        return sharedManager;
    }

}
