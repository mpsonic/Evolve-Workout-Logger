package edu.umn.paull011.evolveworkoutlogger;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Main Android Application Class
 * Initialized to use LeakCanary
 * Created by mitchell on 9/12/16.
 */
public class EvolveApplication extends Application {
    public static RefWatcher getRefWatcher(Context context) {
        EvolveApplication application = (EvolveApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}