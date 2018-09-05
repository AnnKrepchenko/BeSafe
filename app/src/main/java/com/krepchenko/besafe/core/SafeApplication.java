package com.krepchenko.besafe.core;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Created by Ann on 19.10.2015.
 */
public class SafeApplication extends Application {

    private SharedManager sharedManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedManager = new SharedManager(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.setFirestoreSettings(settings);
    }

    public SharedManager getSharedManager() {
        return sharedManager;
    }

}
