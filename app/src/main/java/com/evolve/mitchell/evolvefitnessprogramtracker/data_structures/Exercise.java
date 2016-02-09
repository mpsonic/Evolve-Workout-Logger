package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class to keep track of stats for a specific exercise
 *
 */
public class Exercise {


    // Constructors
    public Exercise(){
        mName = "";
        mId = -1;
        mIsImperialUnits = true;
        mCategoryToIncrement = null;
        mTrackedMeasurements = new ArrayList<>(4);
        mCurrentExerciseSession = null;
        mPreviousExerciseSessions = new ArrayList<>(4);
        mMeasurementMaximums = new ArrayList<>(4);
        mGoalIncreasesPerSession = new ArrayList<>(4);
    }


    public String getName(){
        return mName;
    }


    public void setName(String n){
        mName = n;
    }


    public long getId(){
        return mId;
    }


    public void setId(int newId){
        mId = newId;
    }


    public boolean isImperial(){
        return mIsImperialUnits;
    }


    public void setImperialUnits(){
        mIsImperialUnits = true;
    }


    public void setMetricUnits(){
        mIsImperialUnits = false;
    }


    public void setCategoryToIncrement(MeasurementCategory type){
        mCategoryToIncrement = type;
    }


    public MeasurementCategory getCategoryToIncrement(){
        return mCategoryToIncrement;
    }


    public int getNumTrackedMeasurements(){
        return mTrackedMeasurements.size();
    }


    // Gets the tracked measurement data by the measurement type (can return null)
    public MeasurementData getTrackedMeasurementData(MeasurementCategory measurementCategory){
        MeasurementData data;
        for(int i = 0; i < mTrackedMeasurements.size(); i++){
            data = mTrackedMeasurements.get(i);
            if (data.getCategory() == measurementCategory)
                return data;
        }
        return null;
    }

    public MeasurementData getTrackedMeasurementData(int index) throws Exception{
        int numTrackedMeasurements = mTrackedMeasurements.size();
        if (index >= 0 && index < numTrackedMeasurements ){
            return mTrackedMeasurements.get(index);
        }
        else{
            throw new Exception("Tracked measurement index " +
                    index + " out of range: 0 - " + (numTrackedMeasurements-1));
        }
    }


    // Keep tracked measurements list sorted as measurements are added (max size 4)
    public void trackNewMeasurementCategory(MeasurementCategory measurementCategory){
        MeasurementData newData = new MeasurementData(
                measurementCategory, measurementCategory.getDefaultUnit(mIsImperialUnits), 0);
        if (mTrackedMeasurements.size() == 0)
            mTrackedMeasurements.add(newData);
        else {

            for (MeasurementData data: mTrackedMeasurements){
                if (data.getCategory() == measurementCategory)
                    return;
            }
            int i = 0;
            while ((i < mTrackedMeasurements.size()) && (mTrackedMeasurements.get(i).getCategory().value() < measurementCategory.value()))
                i++;
            mTrackedMeasurements.add(i, newData);
        }
    }


    // Remove a measurement from the tracked measurements list
    public void unTrackMeasurementCategory(MeasurementCategory deleteCategory){
        MeasurementData deleteData = null;
        MeasurementData data;
        for(int i = 0; i < mTrackedMeasurements.size(); i++){
            data = mTrackedMeasurements.get(i);
            if (data.getCategory() == deleteCategory) {
                mTrackedMeasurements.remove(i);
                return;
            }
        }
    }

    // Set the unit for a tracked measurement
    public void setTrackedMeasurementUnit(MeasurementCategory type, Unit unit) throws Exception {
        MeasurementData data = getTrackedMeasurementData(type);
        if (data != null){
            data.setUnit(unit);
        }
        else{
            throw new Exception("Measurement category not currently being tracked");
        }
    }


    public void setTrackedMeasurementValue(MeasurementCategory type, double value) throws Exception{
        MeasurementData data = getTrackedMeasurementData(type);
        if (data != null){
            data.setMeasurement(value);
        }
        else{
            throw new Exception("Measurement category not currently being tracked");
        }
    }


    public double getGoalIncrease(MeasurementCategory goalCategory){
        Pair<MeasurementCategory, Double> categoryIncreasePair;
        for (int i = 0; i < mGoalIncreasesPerSession.size(); i++){
            categoryIncreasePair = mGoalIncreasesPerSession.get(i);
            if (categoryIncreasePair.first == goalCategory){
                return categoryIncreasePair.second;
            }
        }
        return 0;
    }


    public void setGoalIncrease(MeasurementCategory goalCategory, double goalIncrease){
        int numGoals = mGoalIncreasesPerSession.size();
        Pair<MeasurementCategory, Double> oldGoal;
        Pair<MeasurementCategory, Double> newGoal =
                new Pair<>(goalCategory, goalIncrease);
        for (int i = 0; i < numGoals; i++){
            oldGoal = mGoalIncreasesPerSession.get(i);
            if (oldGoal.first == goalCategory){
                mGoalIncreasesPerSession.remove(i);
                mGoalIncreasesPerSession.add(newGoal);
                return;
            }
        }
        mGoalIncreasesPerSession.add(newGoal);
    }


    public double getMeasurementMaximum(MeasurementCategory basisCategory){
        double max = 0;
        Pair<MeasurementCategory, Double> categoryMaxPair;
        for (int i = 0; i < mMeasurementMaximums.size(); i++){
            categoryMaxPair = mMeasurementMaximums.get(i);
            if(categoryMaxPair.first == basisCategory){
                max = categoryMaxPair.second;
            }
        }
        return max;
    }


    public void setMeasurementMaximum(MeasurementCategory basisCategory, double newMeasurement){
        Pair<MeasurementCategory, Double> categoryDoublePair;
        Pair<MeasurementCategory, Double> newMeasurementBasis =
                new Pair<>(basisCategory, newMeasurement);
        for (int i = 0; i < mMeasurementMaximums.size(); i++){
            categoryDoublePair = mMeasurementMaximums.get(i);
            if (categoryDoublePair.first == basisCategory){
                mMeasurementMaximums.remove(i);
                mMeasurementMaximums.add(newMeasurementBasis);
                return;
            }
        }
        mMeasurementMaximums.add(newMeasurementBasis);
    }


    public ExerciseSession getCurrentExerciseSession(){
        return mCurrentExerciseSession;
    }


    // Make a new Exercise Session based off of previous exercise session
    public void createNewExerciseSession(boolean increment){
        if (mCurrentExerciseSession != null){
            mPreviousExerciseSessions.add(mCurrentExerciseSession);
            ExerciseSession template = mCurrentExerciseSession;
            mCurrentExerciseSession = new ExerciseSession(this);

            // Choose to either increment values from last session or not
            if (increment){
                MeasurementCategory incrementCategory;
                double increase = getGoalIncrease(mCategoryToIncrement);
                if (mCategoryToIncrement == null)
                    incrementCategory = mTrackedMeasurements.get(0).getCategory();
                else
                    incrementCategory = mCategoryToIncrement;
                mCurrentExerciseSession.copySetInfoAndIncrement(template, incrementCategory, increase);
            }
            else
                mCurrentExerciseSession.copySetInfo(template);
        }
        else{
            mCurrentExerciseSession = new ExerciseSession(this);
        }
    }

    // Private
    private String mName;
    private long mId;
    private boolean mIsImperialUnits;
    private MeasurementCategory mCategoryToIncrement;
    private List<MeasurementData> mTrackedMeasurements;
    private ExerciseSession mCurrentExerciseSession;
    private List<ExerciseSession> mPreviousExerciseSessions;

    // How much the measurement should increase by for each completed session
    private List<Pair<MeasurementCategory, Double>> mGoalIncreasesPerSession;

    // Use to keep track of highest recorded measurements
    private List<Pair<MeasurementCategory, Double>> mMeasurementMaximums;
}