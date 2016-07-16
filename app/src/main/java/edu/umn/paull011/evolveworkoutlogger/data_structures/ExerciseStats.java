package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
        if (category == MeasurementCategory.REPS) {
            return total / mSetData.size();
        } else {
            int count = mCounts[index];
            return total / count;
        }
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

    class SortedDateStringList {
        ArrayList<Date> sortedDates = new ArrayList<>(4);
        DateFormat dateFormat;

        public SortedDateStringList(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public boolean isEmpty() {
            return sortedDates.size() == 0;
        }

        public boolean add(Date date) {
            int size = sortedDates.size();
            Date d1, d2;
            if (this.contains(date)) {
                return false;
            }
            if (size == 1) {
                d1 = sortedDates.get(0);
                if (d1.compareTo(date) > 0) {
                    sortedDates.add(date);
                } else {
                    sortedDates.add(0, date);
                }
                return true;
            } else if (size > 1) {
                for (int i = 0; i < sortedDates.size() - 1; i++) {
                    d1 = sortedDates.get(i);
                    d2 = sortedDates.get(i + 1);
                    if (d1.compareTo(date) > 0 && d2.compareTo(date) < 0) {
                        sortedDates.add(i + 1, date);
                        return true;
                    }
                }
            }
            sortedDates.add(date);
            return true;
        }

        public String getDateString(int i) {
            return dateFormat.format(sortedDates.get(i));
        }

        public boolean contains(Date date) {
            String newDateString = dateFormat.format(date);
            for (Date d : sortedDates) {
                if (newDateString.equals(dateFormat.format(d))) {
                    return true;
                }
            }
            return false;
        }
    }
}
