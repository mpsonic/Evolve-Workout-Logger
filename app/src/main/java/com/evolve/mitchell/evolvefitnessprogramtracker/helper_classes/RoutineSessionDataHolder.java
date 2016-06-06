package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;

/**
 * A singleton class for passing RoutineSession information between activities
 *
 * Created by Mitchell on 5/31/2016.
 */
public class RoutineSessionDataHolder {

    private static final RoutineSessionDataHolder ourInstance = new RoutineSessionDataHolder();
    private Routine mRoutine;
    private RoutineSession mRoutineSession;

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
