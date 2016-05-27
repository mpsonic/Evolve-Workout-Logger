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

    // Private
    private String mName;
    private String mDescription;
    private ArrayList<Exercise> mExerciseList;

    // Public
    public Routine(){
        this.mName = "";
        mExerciseList = new ArrayList<>();
    }
    public Routine(String name){
        this.mName = name;
        mExerciseList = new ArrayList<>();
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
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


    public RoutineSession createNewRoutineSession(){
        return new RoutineSession(this, true);
    }
}
