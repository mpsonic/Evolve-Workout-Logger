package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class contains info about one exercise session
 *
 */
public class ExerciseSession {

    // Private
    private Exercise mExercise;
    private int mId;
    private Date mDate;
    private ArrayList<Set> mSetList;
    private int mCurrentSetIndex;
    private int mCompletedSets;
    private Boolean mCompleted;
    private static final String TAG = ExerciseSession.class.getSimpleName();


    // Public
    public ExerciseSession(Exercise exercise, boolean createSets){
        mExercise = exercise;
        mId = -1;
        mDate = new Date(Calendar.getInstance().getTimeInMillis());
        mSetList = new ArrayList<>();
        mCompleted = false;
        mCurrentSetIndex = 0;
        mCompletedSets = 0;
        if (createSets) {
            ExerciseSession template = exercise.getMostRecentExerciseSession();
            if (template != null && template.getNumSets() != 0) {
                MeasurementCategory incrementCategory = exercise.getCategoryToIncrement();
                if (incrementCategory != null) {
                    float increment = exercise.getIncrement();
                    this.copySetInfoAndIncrement(template, incrementCategory, increment);
                }
                else {
                    this.copySetInfo(template);
                }
            }
            else {
                Set firstSet = exercise.generateInitialSet();
                mSetList.add(firstSet);
            }
        }
    }


    public String getExerciseName(){
        return mExercise.getName();
    }


    public Unit getUnit(MeasurementCategory category) {
        return mExercise.getUnit(category);
    }


    public Date getDate() {
        return mDate;
    }


    public void setDate(Date date) {
        mDate = date;
    }


    public void setId(int id) {
        mId = id;
    }

    public int getId(){
        return mId;
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


    public boolean hasCategory(MeasurementCategory category) {
        return mExercise.isTracked(category);
    }


    public int getSetProgress(){
        int numSets = mSetList.size();
        int numSetsCompleted = mCompletedSets;
        if (numSets != 0) {
            return (int)(100*((float)numSetsCompleted/numSets));
        }
        return 0;
    }


    // Create a new set based on the last set in the set list, or the exercise's tracked measurements
    public void generateNewSet(){
        Set newSet = new Set();
        int size = mSetList.size();
        if (size == 0){
            for (MeasurementCategory category: MeasurementCategory.values()){
                if (mExercise.isTracked(category)) {
                    MeasurementData newData = new MeasurementData(
                            category,
                            category.getDefaultMeasurement()
                    );
                    newSet.addMeasurement(newData);
                }
            }
        }
        else{
            newSet.copyMeasurementInfo(mSetList.get(size - 1));
        }
        mSetList.add(newSet);
        if (mCompleted) {
            mCompleted = false;
            mCurrentSetIndex = this.getNumSets() - 1;
        }
    }


    public void addSet(Set set) {
        mSetList.add(set);
    }


    public void removeSet(int index){
        mSetList.remove(index);
    }


    public Set getSet(int index){
        return mSetList.get(index);
    }

    public Set getCurrentSet() {
        return mSetList.get(mCurrentSetIndex);
    }

    public int getCurrentSetIndex(){
        return mCurrentSetIndex;
    }


    // finish the current set and move pointer to the next set
    public void completeCurrentSetAndMoveToNext(){
        if (!this.isCompleted() && this.getNumSets() > 0) {
            Set currentSet = this.getSet(mCurrentSetIndex);
            if (!currentSet.isCompleted()){
                currentSet.finish();
                mCompletedSets++;
            }
            if (mCurrentSetIndex != getNumSets() - 1){
                mCurrentSetIndex++;
            }
            if (this.getSetProgress() == 100) {
                mCompleted = true;
            }
        }
    }


    // Copy info from an old exercise session into this one
    public void copySetInfo(ExerciseSession template){
        mSetList.clear();
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
    public void copySetInfoAndIncrement(ExerciseSession template, MeasurementCategory incrementCategory, float increment){
        mSetList.clear();
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
        // Do the sessions have the same sets?
        if (mSetList.size() != other.mSetList.size()){
            return false;
        }
        if (this.isCompleted() != other.isCompleted()){
            return false;
        }
        for(int i = 0; i < mSetList.size(); i++){
            if (!mSetList.get(i).equals(other.mSetList.get(i)))
                return false;
        }
        return true;
    }
}
