package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;

/**
 * Created by Mitchell on 1/18/2016.
 *
 * Service that handles changes / progress to the currently active routine session.
 *
 */
public class RoutineSessionService extends Service {

    // Binder that clients receive
    private final IBinder mBinder = new LocalBinder();

    // The routine session to be manipulated
    private RoutineSession mRoutineSession;

    //Class used for the client Binder.
    public class LocalBinder extends Binder {
        public RoutineSessionService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RoutineSessionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setRoutineSession(RoutineSession session) {
        mRoutineSession = session;
    }

    public RoutineSession getRoutineSession() {
        return mRoutineSession;
    }

}
