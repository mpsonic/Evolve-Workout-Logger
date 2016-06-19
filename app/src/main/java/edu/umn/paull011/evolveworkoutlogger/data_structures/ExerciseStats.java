package edu.umn.paull011.evolveworkoutlogger.data_structures;
import java.sql.Date;

/**
 * A class for holding statistics about an Exercise
 *
 * Created by Mitchell on 6/18/2016.
 */
public class ExerciseStats {

    private boolean[] mTracked;
    private double[] mTotals;
    private int[] mCounts;
    private double[] mMaximums;
    private int timesPerformed;
    private Date dateLastPerformed;

    public ExerciseStats() {
        mTracked = new boolean[] {false,false,false,false};
        mTotals = new double[] {0,0,0,0};
        mCounts = new int[] {0,0,0,0};
        mMaximums = new double[] {0,0,0,0};
    }

    public void add(MeasurementCategory category, double amount) {
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

    public double getAvg(MeasurementCategory category) {
        int index = category.value();
        int count = mCounts[index];
        double total = mTotals[index];
        return total/count;
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

    public void setTimesPerformed(int count) {
        timesPerformed = count;
    }

    public int getTimesPerformed() {
        return timesPerformed;
    }

    public void setDateLastPerformed(Date date) {
        dateLastPerformed = date;
    }

    public Date getDateLastPerformed() {
        return dateLastPerformed;
    }
}
