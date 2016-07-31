package edu.umn.paull011.evolveworkoutlogger.data_structures;

import android.util.Pair;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.umn.paull011.evolveworkoutlogger.helper_classes.SortedDateStringList;

/**
 * A class for holding statistics about a routine
 *
 * Created by Mitchell on 7/17/2016.
 */
public class RoutineStats {

    private HashMap<String, ArrayList<Pair<String, Integer>>> mRoutineData;
    private SortedDateStringList mSortedDates;
    private DateFormat mDateFormat;

    public RoutineStats() {
        mRoutineData = new HashMap<>(4);
        mDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        mSortedDates = new SortedDateStringList(mDateFormat);
    }

    public void add(Date date, String exerciseName, int setCount) {
        mSortedDates.add(date);
        String dateString = mDateFormat.format(date);
        Pair<String, Integer> exerciseSetCount = new Pair<>(exerciseName, setCount);
        ArrayList<Pair<String, Integer>> setCountList;
        if (mRoutineData.containsKey(dateString)) {
            setCountList = mRoutineData.get(dateString);
            setCountList.add(exerciseSetCount);
        }
        else {
            setCountList = new ArrayList<>(4);
            setCountList.add(exerciseSetCount);
            mRoutineData.put(dateString, setCountList);
        }
    }

    public int getNumDates() {
        return mRoutineData.size();
    }

    public List<Pair<String, Integer>> getExerciseSetCounts(int position) {
        String dateString = mSortedDates.getDateString(position);
        return mRoutineData.get(dateString);
    }

    public String getDateString(int position) {
        return mSortedDates.getDateString(position);
    }

    public String getLastPerformedDateString() {
        if (!isEmpty()) {
            return mSortedDates.getDateString(0);
        }
        else {
            return null;
        }
    }

    public boolean isEmpty(){
        return mRoutineData.isEmpty();
    }
}
