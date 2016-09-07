package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A class that maintains a list of unique ordered dates.
 * Two dates are not unique if their date strings are equal
 * (date string specified by the dateFormat argument)
 *
 * Created by Mitchell on 7/17/2016.
 */
public class SortedDateStringList {
    ArrayList<Date> sortedDates = new ArrayList<>(4);
    DateFormat dateFormat;
    Boolean mSorted = false;

    public SortedDateStringList(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isEmpty() {
        return sortedDates.size() == 0;
    }

    public boolean add(Date date) {
        if (this.contains(date)) {
            return false;
        }
        else {
            sortedDates.add(date);
            mSorted = false;
            return true;
        }
        /*int size = sortedDates.size();
        Date d1, d2;

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
        return true;*/
    }

    public String getDateString(int i) {
        if (!mSorted) {
            Collections.sort(sortedDates, new Comparator<Date>() {
                @Override
                public int compare(Date date, Date d2) {
                    return (int)(d2.getTime() - date.getTime())/(86400);
                }
            });
            mSorted = true;
        }
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
