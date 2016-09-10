package edu.umn.paull011.evolveworkoutlogger.data_structures;

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

    // Private
    private final boolean mUniversal;
    private final boolean mImperial;

    Unit(){
        this.mUniversal = true;
        this.mImperial = true;
    }

    Unit(boolean imperial){
        this.mUniversal = false;
        this.mImperial = imperial;
    }

    /***
     * Get the Unit enum from its name or abbreviation
     * @param name unit name
     * @return unit enum
     */
    public static Unit getFromName(String name) {
        Unit result = null;
        name = name.toUpperCase();
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
            case "LBS":
                result = POUNDS;
                break;
            case "FEET":
                result = FEET;
                break;
            case "FT":
                result = FEET;
                break;
            case "MILES":
                result = MILES;
                break;
            case "MI":
                result = MILES;
                break;
            case "TIME":
                result = TIME;
                break;
            case "SECONDS":
                result = SECONDS;
                break;
            case "S":
                result = SECONDS;
                break;
            case "MINUTES":
                result = MINUTES;
                break;
            case "HOURS":
                result = HOURS;
                break;
            case "H":
                result = HOURS;
                break;
        }
        return result;
    }

    public boolean isUniversal() {
        return mUniversal;
    }

    public boolean isImperial() {
        return mImperial;
    }

    /***
     * Get the name to be displayed (usually an abbreviation) for the unit
     * @return display name
     */
    public String getDisplayName() {
        switch (this) {
            case REPS:
                return "reps";
            case KILOGRAMS:
                return "kg";
            case METERS:
                return "m";
            case KILOMETERS:
                return "km";
            case POUNDS:
                return "lbs";
            case FEET:
                return "ft";
            case MILES:
                return "mi";
            case TIME:
                return "";
            case SECONDS:
                return "s";
            case MINUTES:
                return "m";
            case HOURS:
                return "h";
        }
        return "";
    }
}
