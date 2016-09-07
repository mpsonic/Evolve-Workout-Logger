package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * A set contains data about one iteration of an exercise
 */
public class Set {

    private static final String TAG = Set.class.getSimpleName();
    // Private
    private Map<MeasurementCategory, MeasurementData> mMeasurements;
    private float mPercentage;
    private boolean mCompleted;

    // Public
    public Set(){
        mMeasurements = new HashMap<>(4);
        mCompleted = false;
    }


    // add a new measurement to track in this set
    public void addMeasurement(MeasurementData data){
        MeasurementCategory category = data.getCategory();
        mMeasurements.remove(category);
        mMeasurements.put(category, data);
    }

    public void setMeasurement(MeasurementCategory category, float measurement) {
        if (mMeasurements.containsKey(category)) {
            MeasurementData data = mMeasurements.get(category);
            data.setMeasurement(measurement);
        }
    }

    // remove one of the measurements from the set
    public void deleteMeasurement(MeasurementCategory category){
        mMeasurements.remove(category);
    }


    public MeasurementData getMeasurementData(MeasurementCategory category){
        return mMeasurements.get(category);
    }

    public Collection<MeasurementData> measurements() {
        return mMeasurements.values();
    }

    // take one of the set measurements and add num to it
    public void incrementMeasurement(MeasurementCategory category, float num){
        if (mMeasurements.containsKey(category)) {
            MeasurementData data = mMeasurements.get(category);
            float measurement = data.getMeasurement();
            measurement += num;
            data.setMeasurement(measurement);
        }
    }

    public int getNumMeasurements(){
        return mMeasurements.size();
    }

    // get the percentage value
    public float getPercentage(){
        return mPercentage;
    }


    // change the percentage value for this set
    public void setPercentage(float percentage){
        this.mPercentage = percentage;
    }


    // Has the set been completed?
    public boolean isCompleted(){
        return mCompleted;
    }


    // Complete the set
    public void finish(){
        mCompleted = true;
    }


    public void copyMeasurementInfo(Set oldSet){
        final int size = getNumMeasurements();
        for (MeasurementData oldData : oldSet.mMeasurements.values()) {
            MeasurementData newData = new MeasurementData(
                    oldData.getCategory(),
                    oldData.getMeasurement(),
                    oldData.getUnit()
            );
            addMeasurement(newData);
        }
    }


    public void copyMeasurementInfoAndIncrement(Set oldSet, MeasurementCategory incrementCategory, float increment){
        for (MeasurementData oldData : oldSet.mMeasurements.values()) {
            MeasurementData newData = new MeasurementData();
            newData.copyData(oldData);
            if (incrementCategory == oldData.getCategory()) {
                newData.setMeasurement(oldData.getMeasurement() + increment);
            }
            addMeasurement(newData);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // Objects of the same class?
        if (getClass() != obj.getClass())
            return false;
        Set other = (Set) obj;
        // is the percentage identical
        if (mPercentage != (other.getPercentage()))
            return false;

        // Same number of measurements?
        final int mySize = getNumMeasurements();
        final int otherSize = other.getNumMeasurements();
        if  (mySize != otherSize)
            return false;

        if (this.isCompleted() != other.isCompleted()) {
            return false;
        }
        // is the measurement data identical?
        MeasurementData otherData;
        for (MeasurementData myData : mMeasurements.values()) {
            otherData = other.getMeasurementData(myData.getCategory());
            if(!(myData.equals(otherData)))
                return false;
        }
        return true;
    }
}
