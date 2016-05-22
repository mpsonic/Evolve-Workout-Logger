package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * This class is meant for storing a small unit of data pertaining to a workout.
 * A set is described by a collection of MeasurementParameters
 */

// Finished
public class MeasurementData{

    // Public
    public MeasurementData(){}
    public MeasurementData(MeasurementCategory category, Unit unit, int measurement){
        mCategory = category;
        mUnit = unit;
        mMeasurement = measurement;
    }

    public MeasurementCategory getCategory(){
        return mCategory;
    }


    public Unit getUnit(){
        return mUnit;
    }


    public float getMeasurement(){
        return mMeasurement;
    }


    public void setCategory(MeasurementCategory category){
        mCategory = category;
    }


    public void setUnit(Unit u){
        mUnit = u;
    }


    public void setMeasurement(float m){
        mMeasurement = m;
    }


    public void copyData(MeasurementData other){
        mUnit = other.mUnit;
        mCategory = other.mCategory;
        mMeasurement = other.mMeasurement;
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
        MeasurementData other = (MeasurementData) obj;
        if (mUnit != other.getUnit())
            return false;
        return mCategory == other.mCategory && mMeasurement == other.mMeasurement;
    }

    // Private
    private Unit mUnit;
    private MeasurementCategory mCategory;
    private float mMeasurement;
}
