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

    // Private
    private final boolean mUniversal;
    private final boolean mImperial;
}
