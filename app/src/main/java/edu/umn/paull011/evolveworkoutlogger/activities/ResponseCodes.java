package edu.umn.paull011.evolveworkoutlogger.activities;

/**
 * An enumeration of
 *
 * Created by Mitchell on 5/15/2016.
 */
public enum ResponseCodes {

    NEW_ROUTINE(1),
    NEW_EXERCISE(2),
    EDIT_ROUTINE(3),
    EDIT_EXERCISE(4);

    ResponseCodes(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    private int mValue;
}
