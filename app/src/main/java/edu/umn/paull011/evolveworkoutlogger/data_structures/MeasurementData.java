package edu.umn.paull011.evolveworkoutlogger.data_structures;

import edu.umn.paull011.evolveworkoutlogger.helper_classes.TimeStringHelper;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * This class is meant for storing a small unit of data pertaining to a workout.
 * A set is described by a collection of MeasurementParameters
 */

// Finished
public class MeasurementData{

    private static final String TAG = MeasurementData.class.getSimpleName();
    private MeasurementCategory mCategory;
    private float mMeasurement;
    private Unit mUnit;


    // Public
    public MeasurementData(){}

    public MeasurementData(MeasurementCategory category, float measurement, Unit unit) {
        mCategory = category;
        mMeasurement = measurement;
        mUnit = unit;
    }

    public MeasurementData(MeasurementCategory category, float measurement) {
        mCategory = category;
        mMeasurement = measurement;
        mUnit = category.getDefaultUnit(true);
    }

    public MeasurementCategory getCategory() {
        return mCategory;
    }

    public void setCategory(MeasurementCategory category){
        mCategory = category;
    }

    public float getMeasurement() {
        return mMeasurement;
    }

    public void setMeasurement(float m){
        mMeasurement = m;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public void copyData(MeasurementData other){
        mCategory = other.mCategory;
        mMeasurement = other.mMeasurement;
    }

    public String display() {
        String unit = mUnit.getDisplayName();
        String number = getDisplayNumber();
        if (mCategory == MeasurementCategory.TIME) {
            return number;
        }
        else {
            return number + " " + unit;
        }
    }

    private String getDisplayNumber() {
        if (mCategory == MeasurementCategory.TIME) {
            return TimeStringHelper.createTimeString((int) mMeasurement);
        }
        if (isInteger()) {
            return String.valueOf((int) mMeasurement);
        }
        return String.valueOf(mMeasurement);
    }

    private String getTimeDisplayNumber() {
        if (mUnit == Unit.HOURS) {
            return String.valueOf(mMeasurement / 3600);
        } else if (mUnit == Unit.MINUTES) {
            return String.valueOf(mMeasurement / 60);
        } else {
            return String.valueOf((int) mMeasurement);
        }
    }

    private boolean isInteger() {
        return (Math.round(mMeasurement) == mMeasurement);
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

}
