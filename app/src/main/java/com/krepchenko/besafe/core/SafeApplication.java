package com.krepchenko.besafe.core;

import android.app.Application;

import com.krepchenko.besafe.crypt.CryptManager;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Ann on 19.10.2015.
 */
public class SafeApplication extends Application {

    private SharedManager sharedManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sharedManager = new SharedManager(this);
    }

    public SharedManager getSharedManager() {
        return sharedManager;
    }

}
