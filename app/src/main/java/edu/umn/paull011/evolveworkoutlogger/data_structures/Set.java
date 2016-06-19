package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.util.LinkedList;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * A set contains data about one iteration of an exercise
 */
public class Set {

    // Private
    private LinkedList<MeasurementData> mMeasurementList;
    private float mPercentage;
    private boolean mCompleted;
    private static final String TAG = Set.class.getSimpleName();

    // Public
    public Set(){
        mMeasurementList = new LinkedList<>();
        mCompleted = false;
    }


    // add a new measurement to track in this set
    public void addMeasurement(MeasurementData data){
        deleteMeasurement(data.getCategory());
        mMeasurementList.add(data);
    }

    public void setMeasurement(MeasurementCategory category, float measurement) {
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                data.setMeasurement(measurement);
            }
        }
    }

    // remove one of the measurements from the set
    public void deleteMeasurement(MeasurementCategory category){
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


    // take one of the set measurements and add num to it
    public void incrementMeasurement(MeasurementCategory category, float num){
        for (MeasurementData data: mMeasurementList) {
            if (data.getCategory() == category) {
                float measurement = data.getMeasurement();
                measurement += num;
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
            MeasurementData newData = new MeasurementData(
                    oldData.getCategory(),
                    oldData.getMeasurement()
            );
            addMeasurement(newData);
        }
    }


    public void copyMeasurementInfoAndIncrement(Set oldSet, MeasurementCategory incrementCategory, float increment){
        for (MeasurementData oldData: oldSet.mMeasurementList) {
            MeasurementData newData = new MeasurementData();
            newData.setCategory(oldData.getCategory());
            if (incrementCategory == oldData.getCategory())
                newData.setMeasurement(oldData.getMeasurement() + increment);
            else
                newData.setMeasurement(oldData.getMeasurement());
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
        for (MeasurementData myData: mMeasurementList) {
            otherData = other.getMeasurementData(myData.getCategory());
            if(!(myData.equals(otherData)))
                return false;
        }
        return true;
    }
}
