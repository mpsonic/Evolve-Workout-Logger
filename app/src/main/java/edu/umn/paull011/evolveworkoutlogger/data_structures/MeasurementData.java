package edu.umn.paull011.evolveworkoutlogger.data_structures;

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
    public MeasurementData(MeasurementCategory category, float measurement){
        mCategory = category;
        mMeasurement = measurement;
    }

    public MeasurementCategory getCategory(){
        return mCategory;
    }


    public float getMeasurement(){
        return mMeasurement;
    }


    public void setCategory(MeasurementCategory category){
        mCategory = category;
    }


    public void setMeasurement(float m){
        mMeasurement = m;
    }


    public void copyData(MeasurementData other){
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
        return mCategory == other.mCategory && mMeasurement == other.mMeasurement;
    }

    // Private
    private MeasurementCategory mCategory;
    private float mMeasurement;
    private static final String TAG = MeasurementData.class.getSimpleName();

}
