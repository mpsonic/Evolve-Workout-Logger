package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;

/**
 * A singleton class for passing ExerciseSession information between components of the
 * ActiveExerciseSession Activity
 * Created by mitchell on 9/9/16.
 */
public class ExerciseSessionDataHolder {

    private static final ExerciseSessionDataHolder ourInstance = new ExerciseSessionDataHolder();
    private Exercise mExercise;
    private ExerciseSession mExerciseSession;
    private static final String TAG = ExerciseSessionDataHolder.class.getSimpleName();

    public static ExerciseSessionDataHolder getInstance() { return ourInstance; }

    private ExerciseSessionDataHolder() {}

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public void setExerciseSession(ExerciseSession exerciseSession) {
        mExerciseSession = exerciseSession;
    }

    public ExerciseSession getExerciseSession() {
        return mExerciseSession;
    }
}
