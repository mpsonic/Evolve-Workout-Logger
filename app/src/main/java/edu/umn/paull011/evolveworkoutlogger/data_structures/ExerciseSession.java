package edu.umn.paull011.evolveworkoutlogger.data_structures;

import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import edu.umn.paull011.evolveworkoutlogger.BuildConfig;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class contains info about one exercise session
 *
 */
public class ExerciseSession {

    private static final String TAG = ExerciseSession.class.getSimpleName();
    // Private
    private Exercise mExercise;
    private int mId;
    private Date mDate;
    private ArrayList<Set> mSetList;
    private int mCurrentSetIndex;
    private Boolean mCompleted;
    private OnExerciseSessionUpdateListener mUpdateListener;

    public interface OnExerciseSessionUpdateListener {
        void onExerciseSessionUpdate();
    }

    private class DummyUpdatedListener implements OnExerciseSessionUpdateListener {
        @Override
        public void onExerciseSessionUpdate() {}
    }

    // Public
    public ExerciseSession(Exercise exercise, boolean createSets){
        Log.d(TAG,"ExerciseSession");
        mExercise = exercise;
        mId = -1;
        mDate = new Date(Calendar.getInstance().getTimeInMillis());
        mSetList = new ArrayList<>();
        mCompleted = false;
        mCurrentSetIndex = 0;
        mUpdateListener = new DummyUpdatedListener();
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

    public void setOnExerciseSessionUpdatedListener(OnExerciseSessionUpdateListener listener) {
        Log.d(TAG,"setOnExerciseSessionUpdatedListener");
        mUpdateListener = listener;
    }

    public void removeExerciseSessionUpdateListener() {
        Log.d(TAG,"removeExerciseSessionUpdateListener");
        mUpdateListener = new DummyUpdatedListener();
    }

    public String getExerciseName(){
        Log.d(TAG,"getExerciseName");
        return mExercise.getName();
    }


    public Unit getUnit(MeasurementCategory category) {
        Log.d(TAG,"getUnit");
        return mExercise.getUnit(category);
    }


    public Date getDate() {
        Log.d(TAG,"getDate");
        return mDate;
    }


    public void setDate(Date date) {
        Log.d(TAG,"setDate");
        mDate = date;
    }

    public int getId() {
        Log.d(TAG,"getId");
        return mId;
    }

    public void setId(int id) {
        Log.d(TAG,"setId");
        mId = id;
    }

    public boolean isCompleted(){
        Log.d(TAG,"isCompleted");
        return mCompleted;
    }

    public void finish(){
        Log.d(TAG,"finish");
        mCompleted = true;
    }


    public int getNumSets(){
        Log.d(TAG,"getNumSets");
        return mSetList.size();
    }


    public boolean hasCategory(MeasurementCategory category) {
        Log.d(TAG,"hasCategory");
        return mExercise.isTracked(category);
    }


    public int getSetProgress(){
        Log.d(TAG,"getSetProgress");
        int numSets = mSetList.size();
        int numSetsCompleted = 0;
        for (Set set: mSetList) {
            if (set.isCompleted()) {
                numSetsCompleted++;
            }
        }
        if (numSets != 0) {
            return (int)(100*((float)numSetsCompleted/numSets));
        }
        return 0;
    }


    // Create a new set based on the last set in the set list, or the exercise's tracked measurements
    public void generateNewSet(){
        Log.d(TAG,"generateNewSet");
        Set newSet = new Set();
        int size = mSetList.size();
        if (size == 0){
            for (MeasurementCategory category: MeasurementCategory.values()){
                if (mExercise.isTracked(category)) {
                    MeasurementData newData = new MeasurementData(
                            category,
                            mExercise.getInitialMeasurementValue(category),
                            mExercise.getUnit(category)
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
        mUpdateListener.onExerciseSessionUpdate();
    }


    public void addSet(Set set) {
        Log.d(TAG,"addSet");
        mSetList.add(set);
        mUpdateListener.onExerciseSessionUpdate();
    }


    public void removeSet(int index){
        Log.d(TAG,"removeSet");
        mSetList.remove(index);
        refreshCurrentSetIndex();
        mUpdateListener.onExerciseSessionUpdate();
    }


    public Set getSet(int index){
        Log.d(TAG,"getSet");
        return mSetList.get(index);
    }

    public Set getCurrentSet() {
        Log.d(TAG,"getCurrentSet");
        return mSetList.get(mCurrentSetIndex);
    }

    public int getCurrentSetIndex(){
        Log.d(TAG,"getCurrentSetIndex");
        return mCurrentSetIndex;
    }


    // finish the current set and move pointer to the next set
    public void completeCurrentSetAndMoveToNext(){
        Log.d(TAG,"completeCurrentSetAndMoveToNext");

        if (!this.isCompleted() && this.getNumSets() > 0) {
            Set currentSet = this.getSet(mCurrentSetIndex);
            if (!currentSet.isCompleted()){
                currentSet.finish();
                if (mCurrentSetIndex == 0) {
                    for (MeasurementCategory category: MeasurementCategory.values()) {
                        if (currentSet.hasCategory(category)) {
                            mExercise.addInitialMeasurementData(currentSet.getMeasurementData(category));
                        }
                    }
                }
            }
            if (mCurrentSetIndex != getNumSets() - 1){
                mCurrentSetIndex++;
            }
            if (this.getSetProgress() == 100) {
                mCompleted = true;
            }
        }
        mUpdateListener.onExerciseSessionUpdate();
    }


    // Copy info from an old exercise session into this one
    public void copySetInfo(ExerciseSession template){
        Log.d(TAG,"copySetInfo");
        mSetList.clear();
        int numSets = template.getNumSets();
        Set oldSet;
        for (int i = 0; i < numSets; i++){
            oldSet = template.getSet(i);
            Set newSet = new Set();
            newSet.copyMeasurementInfo(oldSet);
            if (oldSet.isCompleted()) {
                mSetList.add(newSet);
            }
        }
        refreshCurrentSetIndex();
        mUpdateListener.onExerciseSessionUpdate();
    }


    // Copy info from an old exercise session into this one and increment the appropriate measurement
    public void copySetInfoAndIncrement(ExerciseSession template, MeasurementCategory incrementCategory, float increment){
        Log.d(TAG,"copySetInfoAndIncrement");
        mSetList.clear();
        int numSets = template.getNumSets();
        Set oldSet;
        for (int i = 0; i < numSets; i++){
            oldSet = template.getSet(i);
            Set newSet = new Set();
            newSet.copyMeasurementInfoAndIncrement(oldSet, incrementCategory, increment);
            mSetList.add(newSet);
        }
        refreshCurrentSetIndex();
        mUpdateListener.onExerciseSessionUpdate();
    }

    public void refreshCurrentSetIndex() {
        Log.d(TAG,"refreshCurrentSetIndex");
        int currentSetIndex = -1;
        if (mSetList.size() == 0) {
            return;
        }
        Set set;
        do {
            currentSetIndex++;
            set = mSetList.get(currentSetIndex);
        } while (set.isCompleted() && currentSetIndex < mSetList.size() - 1);
        if (currentSetIndex == mSetList.size() - 1 && set.isCompleted()) {
            currentSetIndex++;
            mCompleted = true;
        }
        else {
            mCompleted = false;
        }
        mCurrentSetIndex = currentSetIndex;
    }

    public void swapSets(int fromPosition, int toPosition) {
        Log.d(TAG,"swapSets");
        if (BuildConfig.DEBUG) {
            if (fromPosition < mSetList.size()) {
                throw new AssertionError();
            }
            if (fromPosition < mSetList.size()) {
                throw new AssertionError();
            }
            if (toPosition < mSetList.size()) {
                throw new AssertionError();
            }
            if (toPosition < mSetList.size()) {
                throw new AssertionError();
            }
            if (fromPosition != toPosition) {
                throw new AssertionError();
            }
        }
        Set from = mSetList.get(fromPosition);
        Set to = mSetList.get(toPosition);
        mSetList.set(fromPosition, to);
        mSetList.set(toPosition, from);
        mUpdateListener.onExerciseSessionUpdate();
    }

    @Override
    public boolean equals(Object obj){
        Log.d(TAG,"equals");
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
