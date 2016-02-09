package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class contains info about one exercise session
 *
 */
public class ExerciseSession {

    // Public
    public ExerciseSession(){
        mDate = Calendar.getInstance().getTime();
        mSetList = new ArrayList<>();
        mBaseSetMeasurements = new ArrayList<>();
        mCompleted = false;
    }
    public ExerciseSession(Exercise e){
        mExerciseName = e.getName();
        mExerciseId = e.getId();
        mDate = Calendar.getInstance().getTime();
        // Fill mSetList based on template from e
        mSetList = new ArrayList<>();
        mCompleted = false;
        mCurrentSetIndex = 0;
        mCompletedSets = 0;
        mBaseSetMeasurements = new ArrayList<>(4);
        MeasurementData trackedData;
        for (int i = 0; i < e.getNumTrackedMeasurements(); i++){
            try{
                trackedData = e.getTrackedMeasurementData(i);
                mBaseSetMeasurements.add(trackedData);
            }
            catch (Exception exception){}
        }
    }


    public String getName(){
        return mExerciseName;
    }


    public Date getDate(){
        return mDate;
    }


    public long getExerciseId(){
        return mExerciseId;
    }


    public boolean isCompleted(){
        return mCompleted;
    }

    public void finish(){
        mCompleted = true;
    }


    public int getNumSets(){
        return mSetList.size();
    }


    public int getNumCompletedSets(){
        return mCompletedSets;
    }


    public int getSetProgress(){
        int numSets = mSetList.size();
        int numSetsCompleted = mCompletedSets;
        return 100*(numSetsCompleted/numSets);
    }


    // Create a new set based on the last set in the set list, or the exercise's tracked measurements
    public void generateNewSet(){
        Set newSet = new Set();
        int size = mSetList.size();
        if (size == 0){
            for (MeasurementData data: mBaseSetMeasurements){
                MeasurementData newData = new MeasurementData();
                newData.copyData(data);
                newSet.add_measurement(newData);
            }
        }
        else{
            newSet.copyMeasurementInfo(mSetList.get(size - 1));
        }
        mSetList.add(newSet);
    }


    public void removeSet(int index){
        mSetList.remove(index);
    }


    public Set getSet(int index){
        return mSetList.get(index);
    }


    public Set getCurrentSet(){
        return getSet(mCurrentSetIndex);
    }


    // finish the current set and move pointer to the next set
    public void completeCurrentSet(){
        Set currentSet = getSet(mCurrentSetIndex);
        if (!currentSet.isCompleted()){
            currentSet.finish();
            mCompletedSets++;
        }
        if (mCurrentSetIndex != getNumSets() - 1){
            mCurrentSetIndex++;
        }
    }


    // Copy info from an old exercise session into this one
    public void copySetInfo(ExerciseSession template){
        int numSets = template.getNumSets();
        Set oldSet;
        for (int i = 0; i < numSets; i++){
            oldSet = template.getSet(i);
            Set newSet = new Set();
            newSet.copyMeasurementInfo(oldSet);
            mSetList.add(newSet);
        }
    }


    // Copy info from an old exercise session into this one and increment the appropriate measurement
    public void copySetInfoAndIncrement(ExerciseSession template, MeasurementCategory incrementCategory, double increment){
        int numSets = template.getNumSets();
        Set oldSet;
        for (int i = 0; i < numSets; i++){
            oldSet = template.getSet(i);
            Set newSet = new Set();
            newSet.copyMeasurementInfoAndIncrement(oldSet, incrementCategory, increment);
            mSetList.add(newSet);
        }
    }


    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // Objects of the same class?
        if (getClass() != obj.getClass())
            return false;
        ExerciseSession other = (ExerciseSession) obj;
        // Do sessions have the same parent exercise?
        if (mExerciseId != other.mExerciseId){
            return false;
        }
        // Do the sessions have the same sets?
        if (mSetList.size() != other.mSetList.size()){
            return false;
        }
        for(int i = 0; i < mSetList.size(); i++){
            if (!mSetList.get(i).equals(other.mSetList.get(i)))
                return false;
        }
        return true;
    }


    // Private
    private String mExerciseName;
    private long mExerciseId;
    private Date mDate;
    private ArrayList<Set> mSetList;
    private int mCurrentSetIndex;
    private int mCompletedSets;
    private Boolean mCompleted;

    // Measurements tracked (weight, reps, time, distance) and starting numbers
    private ArrayList<MeasurementData> mBaseSetMeasurements;

}
