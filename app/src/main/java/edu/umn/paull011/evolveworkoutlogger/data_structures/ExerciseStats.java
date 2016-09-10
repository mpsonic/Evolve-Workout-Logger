package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import edu.umn.paull011.evolveworkoutlogger.helper_classes.SortedDateStringList;

/**
 * A class for holding statistics about an Exercise
 *
 * Created by Mitchell on 6/18/2016.
 */
public class ExerciseStats {

    private HashMap<String, List<Set>> mSetData;
    private DateFormat mDateFormat;
    private SortedDateStringList mSortedDates;
    private boolean[] mTracked;
    private double[] mTotals;
    private int[] mCounts;
    private double[] mMaximums;
    private int timesPerformed;
    private Date dateLastPerformed;
    public ExerciseStats() {
        mSetData = new HashMap<>(4);
        mDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        mSortedDates = new SortedDateStringList(mDateFormat);
        mTracked = new boolean[] {false,false,false,false};
        mTotals = new double[] {0,0,0,0};
        mCounts = new int[] {0,0,0,0};
        mMaximums = new double[] {0,0,0,0};
        dateLastPerformed = new Date(0);
    }

    public void add(MeasurementData data) {
        MeasurementCategory category = data.getCategory();
        float amount = data.getMeasurement();
        int index = category.value();
        if (!mTracked[index]) {
            mTracked[index] = true;
        }
        mTotals[index] +=  amount;
        mCounts[index] += 1;
        double max = mMaximums[index];
        if (amount > max) {
            mMaximums[index] = amount;
        }
    }

    public void addSet(Date date, Set set) {
        String dateString = mDateFormat.format(date);
        if (mSetData.containsKey(dateString)) {
            mSetData.get(dateString).add(set);
        } else {
            LinkedList<Set> setList = new LinkedList<>();
            setList.add(set);
            mSetData.put(dateString, setList);
            mSortedDates.add(date);
        }
        for (MeasurementData data : set.measurements()) {
            add(data);
        }
        if (dateLastPerformed.before(date)) {
            dateLastPerformed = date;
        }
    }

    public double getAvg(MeasurementCategory category) {
        int index = category.value();
        double total = mTotals[index];
        /*if (category == MeasurementCategory.REPS) {
            return total / mSetData.size();
        } else {
            int count = mCounts[index];
            return total / count;
        }*/
        int count = mCounts[index];
        return total / count;
    }

    public double getMax(MeasurementCategory category) {
        int index = category.value();
        return mMaximums[index];
    }

    public boolean isTracked(MeasurementCategory category) {
        return mTracked[category.value()];
    }

    public boolean isEmpty() {
        return !(mTracked[0] || mTracked[1] || mTracked[2] || mTracked[3]);
    }

    public int getTimesPerformed() {
        return mSetData.size();
    }

    public String getLastPerformedDateString() {
        if (!mSortedDates.isEmpty()) {
            return mSortedDates.getDateString(0);
        } else {
            return null;
        }
    }

    public java.util.Set<String> getDates() {
        return mSetData.keySet();
    }

    public String getDateString(int position) {
        return mSortedDates.getDateString(position);
    }

    public List<Set> getSets(int position) {
        String dateString = mSortedDates.getDateString(position);
        return mSetData.get(dateString);
    }

    public int getNumSessions() {
        return mSetData.size();
    }


}
