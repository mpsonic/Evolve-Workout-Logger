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
    public void add_measurement(MeasurementData measurementParameter){
        mMeasurementList.add(measurementParameter);
    }


    // remove one of the measurements from the set
    public void delete_measurement(int index){
        mMeasurementList.remove(index);
    }


    public MeasurementData getMeasurementData(int index){
        if (index >= 0 && index < mMeasurementList.size())
            return mMeasurementList.get(index);
        else
            return null;
    }


    // take one of the set measurements and increase it by num
    public void increment_measurement(int index, double num){
        double measurement = mMeasurementList.get(index).getMeasurement();
        measurement += num;
        mMeasurementList.get(index).setMeasurement(measurement);
    }


    // take one of the set measurements and decrease it by num
    public void decrement_measurement(int index, double num){
        double measurement = mMeasurementList.get(index).getMeasurement();
        measurement -= num;
        mMeasurementList.get(index).setMeasurement(measurement);
    }


    public int getNumMeasurements(){
        return mMeasurementList.size();
    }


    // get the percentage value
    public double getPercentage(){
        return mPercentage;
    }


    // change the percentage value for this set
    public void setPercentage(double percentage){
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
        MeasurementData oldData;
        for (int i = 0; i < size; i++){
            oldData = oldSet.getMeasurementData(i);
            MeasurementData newData = new MeasurementData();
            newData.setCategory(oldData.getCategory());
            newData.setUnit(oldData.getUnit());
            newData.setMeasurement(oldData.getMeasurement());
            add_measurement(newData);
        }
    }


    public void copyMeasurementInfoAndIncrement(Set oldSet, MeasurementCategory incrementCategory, double increment){
        final int size = getNumMeasurements();
        MeasurementData oldData;
        for (int i = 0; i < size; i++) {
            oldData = oldSet.getMeasurementData(i);
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
        MeasurementData myData;
        MeasurementData otherData;
        for (int i = 0; i < mySize; i++) {
            myData = getMeasurementData(i);
            otherData = other.getMeasurementData(i);
            if(!(myData.equals(otherData)))
                return false;
        }
        return true;
    }


    // Private
    private LinkedList<MeasurementData> mMeasurementList;
    private double mPercentage;
    private boolean mCompleted;
}
