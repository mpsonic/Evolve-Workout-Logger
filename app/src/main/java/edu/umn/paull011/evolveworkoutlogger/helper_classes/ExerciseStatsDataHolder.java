package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;

/**
 * A static data holder for the View Exercise activity
 *
 * Created by Mitchell on 6/18/2016.
 */
public class ExerciseStatsDataHolder {

    private static final ExerciseStatsDataHolder ourInstance = new ExerciseStatsDataHolder();
    private Exercise mExercise;
    private ExerciseStats mExerciseStats;
    private static final String TAG = ExerciseStatsDataHolder.class.getSimpleName();


    public static ExerciseStatsDataHolder getInstance() {
        return ourInstance;
    }

    private ExerciseStatsDataHolder() {
    }

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public void setExerciseStats(ExerciseStats exerciseStats) {
        mExerciseStats = exerciseStats;
    }

    public ExerciseStats getExerciseStats() {
        return mExerciseStats;
    }
}
