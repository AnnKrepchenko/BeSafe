package com.krepchenko.besafe.core;

import android.app.Application;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Ann on 19.10.2015.
 */
public class SafeApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QKMTNNkjyfupkA2BwdWjEF2TZ";
    private static final String TWITTER_SECRET = "ASbjvSL9LNbNli9Kv68C4IumXeQa1d5683SuzHCPSJHH3jXEXj";


    private SharedManager sharedManager;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        sharedManager = new SharedManager(this);
    }

    public SharedManager getSharedManager() {
        return sharedManager;
    }

}
