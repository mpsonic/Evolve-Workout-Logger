package edu.umn.paull011.evolveworkoutlogger.data_structures;

/**
 * Created by Mitchell on 12/18/2015.
 *
 * Possible measurement categories to track for an exercise (Reps, Weight, Distance, Time)
 *
 */
public enum MeasurementCategory {
    REPS(0, 5),
    WEIGHT(1, 0),
    DISTANCE(2, 0),
    TIME(3, 300);

    MeasurementCategory(int value, float defaultMeasurement){
        mValue = value;
        mDefaultMeasurement = defaultMeasurement;
    }

    public int value(){
        return mValue;
    }

    public static MeasurementCategory getFromName(String name) {
        MeasurementCategory category = null;
        switch (name) {
            case "REPS":
                category = REPS;
                break;
            case "WEIGHT":
                category = WEIGHT;
            break;
            case "DISTANCE":
                category = DISTANCE;
            break;
            case "TIME":
                category = TIME;
            break;
        }
        return category;
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

    public float getDefaultMeasurement() {
        return mDefaultMeasurement;
    }

    // Private
    private final int mValue;
    private final float mDefaultMeasurement;
}
