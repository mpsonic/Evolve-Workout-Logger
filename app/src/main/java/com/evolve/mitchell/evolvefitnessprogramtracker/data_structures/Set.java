package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import java.util.LinkedList;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * A set contains data about one iteration of an exercise
 */
public class Set {

    // Public
    public Set(){
        mMeasurementList = new LinkedList<>();
        mCompleted = false;
    }


    // add a new measurement to track in this set
    public void add_measurement(MeasurementData measurementData){
        mMeasurementList.add(measurementData);
    }


    // remove one of the measurements from the set
    public void delete_measurement(MeasurementCategory category){
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                mMeasurementList.remove(data);
            }
        }
    }


    public MeasurementData getMeasurementData(MeasurementCategory category){
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                return data;
            }
        }
        return null;
    }


    // take one of the set measurements and increase it by num
    public void increment_measurement(MeasurementCategory category, float num){
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                float measurement = data.getMeasurement();
                measurement += num;
                data.setMeasurement(measurement);
            }
        }
    }

    // take one of the set measurements and decrease it by num
    public void decrement_measurement(MeasurementCategory category, float num){
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                float measurement = data.getMeasurement();
                measurement -= num;
                data.setMeasurement(measurement);
            }
        }
    }


    public int getNumMeasurements(){
        return mMeasurementList.size();
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
        for (MeasurementData oldData: oldSet.mMeasurementList) {
            MeasurementData newData = new MeasurementData();
            newData.setCategory(oldData.getCategory());
            newData.setUnit(oldData.getUnit());
            newData.setMeasurement(oldData.getMeasurement());
            add_measurement(newData);
        }
    }


    public void copyMeasurementInfoAndIncrement(Set oldSet, MeasurementCategory incrementCategory, float increment){
        for (MeasurementData oldData: oldSet.mMeasurementList) {
            MeasurementData newData = new MeasurementData();
            newData.setCategory(oldData.getCategory());
            newData.setUnit(oldData.getUnit());
            if (incrementCategory == oldData.getCategory())
                newData.setMeasurement(oldData.getMeasurement() + increment);
            else
                newData.setMeasurement(oldData.getMeasurement());
            add_measurement(newData);
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

        // is the measurement data identical?
        MeasurementData otherData;
        for (MeasurementData myData: mMeasurementList) {
            otherData = other.getMeasurementData(myData.getCategory());
            if(!(myData.equals(otherData)))
                return false;
        }
        return true;
    }

    // Private
    private LinkedList<MeasurementData> mMeasurementList;
    private float mPercentage;
    private boolean mCompleted;
}
