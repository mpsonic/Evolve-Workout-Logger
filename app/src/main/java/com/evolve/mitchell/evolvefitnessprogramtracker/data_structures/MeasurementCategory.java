package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

/**
 * Created by Mitchell on 12/18/2015.
 *
 * Possible measurement categories to track for an exercise (Reps, Weight, Distance, Time)
 *
 */
public enum MeasurementCategory {
    REPS(0),
    WEIGHT(1),
    DISTANCE(2),
    TIME(3);

    MeasurementCategory(int value){
        mValue = value;
    }

    public int value(){
        return mValue;
    }

    public Unit getDefaultUnit(boolean imperial){
        Unit result;
        switch (mValue){
            case 0:
                result = Unit.REPS;
                break;
            case 1:
                if (imperial)
                    result = Unit.POUNDS;
                else
                    result = Unit.KILOGRAMS;
                break;
            case 2:
                if (imperial)
                    result = Unit.MILES;
                else
                    result = Unit.KILOMETERS;
                break;
            case 3:
                result = Unit.MINUTES;
                break;
            default:
                result = null;
        }
        return result;
    }

    // Private
    private final int mValue;
}
