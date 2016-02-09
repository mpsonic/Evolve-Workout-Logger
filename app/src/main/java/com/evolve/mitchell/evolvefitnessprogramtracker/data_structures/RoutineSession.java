package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class holds data about a routine that was performed on a specific day
 *
 */
public class RoutineSession {

    // Public
    public RoutineSession(Routine routine){
        mParentRoutine = routine;
        mDate = Calendar.getInstance().getTime();
        mCompleted = false;
        int numExercises = routine.getNumExercises();
        mExerciseSessions = new ArrayList<>(numExercises);
        boolean increment;

        // Decide whether to increment appropriate values for each exercise session created
        increment = true;
        for(int i = 0; i < numExercises; i++){
            Exercise exercise = routine.getExercise(i);
            exercise.createNewExerciseSession(increment);
            mExerciseSessions.add(exercise.getCurrentExerciseSession());
        }
    }

    public void addExerciseSession(Exercise exercise, boolean addToRoutine){
        boolean increment = true;
        exercise.createNewExerciseSession(increment);
        mExerciseSessions.add(exercise.getCurrentExerciseSession());
        if(addToRoutine)
            mParentRoutine.addExercise(exercise);
    }


    public void removeExerciseSession(int index){
        mExerciseSessions.remove(index);
    }


    public ExerciseSession getExerciseSession(int index){
        return mExerciseSessions.get(index);
    }


    public int getExerciseCount(){
        return mExerciseSessions.size();
    }


    public Date getDate(){
        return mDate;
    }


    public void setDate(Date date){
        mDate = date;
    }


    public boolean isCompleted() {
        return mCompleted;
    }


    public void finish() {
        mCompleted = true;
    }

    // Private
    private Routine mParentRoutine;
    private Date mDate;
    private boolean mCompleted;
    private ArrayList<ExerciseSession> mExerciseSessions;
}
