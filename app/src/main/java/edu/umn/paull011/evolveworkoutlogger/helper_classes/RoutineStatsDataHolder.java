package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineStats;

/**
 * A static data holder for the View Routine activity
 * Created by Mitchell on 7/17/2016.
 */
public class RoutineStatsDataHolder {
    private static final RoutineStatsDataHolder instance = new RoutineStatsDataHolder();
    private Routine mRoutine;
    private RoutineStats mRoutineStats;
    private static final String TAG = RoutineStatsDataHolder.class.getSimpleName();

    public static RoutineStatsDataHolder getInstance() {
        return instance;
    }

    private RoutineStatsDataHolder() {}

    public Routine getRoutine() {
        return mRoutine;
    }

    public void setRoutine(Routine routine) {
        mRoutine = routine;
    }

    public RoutineStats getRoutineStats() {
        return mRoutineStats;
    }

    public void setRoutineStats(RoutineStats routineStats) {
        mRoutineStats = routineStats;
    }

}
