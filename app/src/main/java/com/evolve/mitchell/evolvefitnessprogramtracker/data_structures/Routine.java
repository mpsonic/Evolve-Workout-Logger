package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import java.util.ArrayList;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * A class for grouping exercises together into a routine.
 * Manages day-by day progression of each routine session
 *
 */
public class Routine {

    // Public
    public Routine(){
        this.mName = "";
        this.mPercentageMode = false;
        mExerciseList = new ArrayList<>();
        mPreviousRoutineSessions = new ArrayList<>();
    }
    public Routine(String name, boolean percentageMode){
        this.mName = name;
        this.mPercentageMode = percentageMode;
        mExerciseList = new ArrayList<>();
        mPreviousRoutineSessions = new ArrayList<>();
    }


    public long getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }


    public void setId(long id) {
        mId = id;
    }


    public void setName(String name) {
        mName = name;
    }


    public void setDescription(String description) {
        mDescription = description;
    }


    public void addExercise(Exercise e){
        mExerciseList.add(e);
    }


    public void removeExercise(int index){
        mExerciseList.remove(index);
    }


    public Exercise getExercise(int index){
        return mExerciseList.get(index);
    }


    public int getNumExercises(){
        return mExerciseList.size();
    }


    public boolean isPercentageMode() {
        return mPercentageMode;
    }


    public void setPercentageMode(boolean mode){
        mPercentageMode = mode;
    }

    public RoutineSession getCurrentRoutineSession(){
        return mCurrentRoutineSession;
    }

    public void createNewRoutineSession(){
        if(mCurrentRoutineSession != null) {
            mPreviousRoutineSessions.add(mCurrentRoutineSession);
        }
        mCurrentRoutineSession = new RoutineSession(this);
    }


    // Private
    private String mName;
    private String mDescription;
    private long mId;
    private boolean mPercentageMode;
    private ArrayList<Exercise> mExerciseList;
    private ArrayList<RoutineSession> mPreviousRoutineSessions;
    private RoutineSession mCurrentRoutineSession;
}
