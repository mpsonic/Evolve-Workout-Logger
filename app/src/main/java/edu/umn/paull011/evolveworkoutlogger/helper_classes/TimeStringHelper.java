package edu.umn.paull011.evolveworkoutlogger.helper_classes;

/**
 * A class for formatting a time string from seconds to the hh:mm:ss format
 * Created by mitchell on 9/9/16.
 */
public class TimeStringHelper {

    /***
     * Get the amount of time in seconds from a time string of the format hh:mm:ss.
     * Will also accept mm:ss input
     * @param timeString Time string to parse
     * @return amount of time in seconds
     */
    public static int parseTimeString(String timeString) {
        String[] timeComponents = timeString.split(":");
        int seconds = 0;
        if (timeComponents.length == 3) { // has hour component
            seconds += Integer.valueOf(timeComponents[0]) * 3600;
            seconds += Integer.valueOf(timeComponents[1]) * 60;
            seconds += Integer.valueOf(timeComponents[2]);
        }
        else if (timeComponents.length == 2) {
            seconds += Integer.valueOf(timeComponents[0]) * 60;
            seconds += Integer.valueOf(timeComponents[1]);
        }
        else if (timeComponents.length == 1) {
            seconds += Integer.valueOf(timeComponents[0]);
        }
        return seconds;
    }

    /***
     * If the given string is shorter than the desired length, pad out the beginning of
     * the string with zeros so that the string has a length of desiredLength
     * @param numString An unpadded number string
     * @param desiredLength The desired length of the string with zeroes prepended
     * @return numString padded with leading zeros
     */
    public static String fillOutLeadingZeros(String numString, int desiredLength) {
        String result = "";
        int numZerosToAdd = desiredLength - numString.length();
        if (numZerosToAdd <= 0) {
            return numString;
        }
        else {
            for (int i = 0; i < numZerosToAdd; i++) {
                result += "0";
            }
            result += numString;
            return result;
        }
    }

    /***
     * Make a time-formatted string from an integer number of seconds
     */
    public static String createTimeString(int seconds) {
        int hours = seconds/3600;
        int minutes = (seconds % 3600)/60;
        seconds = seconds % 60;
        String result;
        if (hours != 0) {
            result = String.valueOf(hours) + ":"
                    + fillOutLeadingZeros(String.valueOf(minutes),2) + ":"
                    + fillOutLeadingZeros(String.valueOf(seconds),2);
        }
        else if (minutes != 0) {
            result = String.valueOf(minutes) + ":"
                    + fillOutLeadingZeros(String.valueOf(seconds),2);
        }
        else {
            result = "0:" + fillOutLeadingZeros(String.valueOf(seconds), 2);
        }
        return result;
    }
}
