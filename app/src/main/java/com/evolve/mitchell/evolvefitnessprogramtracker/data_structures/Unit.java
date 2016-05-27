package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

/**
 *
 * Enumeration for all possible units associated with exercise measurements
 *
 * Created by Mitchell on 12/19/2015.
 */
public enum Unit {

    // Public
    REPS(),
    KILOGRAMS(false),
    METERS(false),
    KILOMETERS(false),
    POUNDS(true),
    FEET(true),
    MILES(true),
    TIME(),
    SECONDS(),
    MINUTES(),
    HOURS();

    Unit(){
        this.mUniversal = true;
        this.mImperial = true;
    }

    Unit(boolean imperial){
        this.mUniversal = false;
        this.mImperial = imperial;
    }

    public boolean isUniversal(){
        return mUniversal;
    }

    public boolean isImperial(){
        return mImperial;
    }

    public static Unit getFromName(String name) {
        Unit result = null;
        switch (name) {
            case "REPS":
                result = REPS;
                break;
            case "KILOGRAMS":
                result = KILOGRAMS;
                break;
            case "METERS":
                result = METERS;
                break;
            case "KILOMETERS":
                result = KILOMETERS;
                break;
            case "POUNDS":
                result = POUNDS;
                break;
            case "FEET":
                result = FEET;
                break;
            case "MILES":
                result = MILES;
                break;
            case "TIME":
                result = TIME;
                break;
            case "SECONDS":
                result = SECONDS;
                break;
            case "MINUTES":
                result = MINUTES;
                break;
            case "HOURS":
                result = HOURS;
                break;
        }
        return result;
    }

    // Private
    private final boolean mUniversal;
    private final boolean mImperial;
}
