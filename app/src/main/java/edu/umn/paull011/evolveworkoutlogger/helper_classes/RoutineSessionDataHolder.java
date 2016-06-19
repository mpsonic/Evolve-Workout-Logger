package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineSession;

/**
 * A singleton class for passing RoutineSession information between activities
 *
 * Created by Mitchell on 5/31/2016.
 */
public class RoutineSessionDataHolder {

    private static final RoutineSessionDataHolder ourInstance = new RoutineSessionDataHolder();
    private Routine mRoutine;
    private RoutineSession mRoutineSession;
    private static final String TAG = RoutineSessionDataHolder.class.getSimpleName();


    public static RoutineSessionDataHolder getInstance() {
        return ourInstance;
    }

    private RoutineSessionDataHolder() {
    }

    public void setRoutine(Routine routine) {
        mRoutine = routine;
    }

    public Routine getRoutine() {
        return mRoutine;
    }

    public void setRoutineSession(RoutineSession session) {
        mRoutineSession = session;
    }

    public RoutineSession getRoutineSession() {
        return mRoutineSession;
    }
}
