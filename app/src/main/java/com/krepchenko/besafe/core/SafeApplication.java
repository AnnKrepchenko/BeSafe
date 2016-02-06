package com.krepchenko.besafe.core;

import android.app.Application;

import com.krepchenko.besafe.crypt.CryptManager;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Ann on 19.10.2015.
 */
public class SafeApplication extends Application {

    private CryptManager cryptManager;
    private SharedManager sharedManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sharedManager = new SharedManager(this);
    }

    public CryptManager getCryptManager() {
        return cryptManager;
    }

    public void setCryptManager(String password) {
        this.cryptManager = new CryptManager(this,password);
    }
    public void cleanCryptManaget(){
        this.cryptManager = null;
    }

    public SharedManager getSharedManager() {
        return sharedManager;
    }
}
