package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class to keep track of stats for a specific exercise
 *
 */
public class Exercise {

    private static final String TAG = Exercise.class.getSimpleName();
    // Private
    private String mName;
    private String mDescription;
    private boolean mIsImperialUnits;
    private String mExerciseCategory;
    private MeasurementCategory mCategoryToIncrement;
    private float mIncrement;
    private int mIncrementPeriod;
    private List<Unit> mUnits;
    private List<MeasurementCategory> mTrackedMeasurementCategories;
    private Set mInitialMeasurementsSet;
    private ExerciseSession mMostRecentExerciseSession;


    // Constructors
    public Exercise(){
        mName = "";
        mDescription = "";
        mIsImperialUnits = true;
        mExerciseCategory = "";
        mCategoryToIncrement = null;
        mIncrement = 0;
        mIncrementPeriod = 1;
        mUnits = new ArrayList<>(4);
        mUnits.add(MeasurementCategory.REPS.getDefaultUnit(true));
        mUnits.add(MeasurementCategory.WEIGHT.getDefaultUnit(true));
        mUnits.add(MeasurementCategory.DISTANCE.getDefaultUnit(true));
        mUnits.add(MeasurementCategory.TIME.getDefaultUnit(true));
        mTrackedMeasurementCategories = new ArrayList<>(4);
        mInitialMeasurementsSet = new Set();
        /*mInitialMeasurementValues = new ArrayList<>(4);
        mInitialMeasurementValues.add(MeasurementCategory.REPS.value(), MeasurementCategory.REPS.getDefaultMeasurement());
        mInitialMeasurementValues.add(MeasurementCategory.WEIGHT.value(), MeasurementCategory.WEIGHT.getDefaultMeasurement());
        mInitialMeasurementValues.add(MeasurementCategory.DISTANCE.value(), MeasurementCategory.DISTANCE.getDefaultMeasurement());
        mInitialMeasurementValues.add(MeasurementCategory.TIME.value(), MeasurementCategory.TIME.getDefaultMeasurement());*/
        mMostRecentExerciseSession = null;
    }


    public String getName(){
        return mName;
    }


    public void setName(String n){
        mName = n;
    }


    public String getDescription() {
        return mDescription;
    }


    public void setDescription(String description) {
        mDescription = description;
    }


    public boolean isImperial(){
        return mIsImperialUnits;
    }


    public void setImperialUnits(){
        mIsImperialUnits = true;
    }


    public void setMetricUnits(){
        mIsImperialUnits = false;
    }

    public String getExerciseCategory() {
        return mExerciseCategory;
    }

    public void setExerciseCategory(String category) {
        mExerciseCategory = category;
    }

    public void setMeasurementCategoryToIncrement(MeasurementCategory category){
        mCategoryToIncrement = category;
    }

    public float getIncrement() {
        return mIncrement;
    }

    public void setIncrement(float increment) {
        mIncrement = increment;
    }

    public int getIncrementPeriod() {
        return mIncrementPeriod;
    }


    public void setIncrementPeriod(int period) {
        mIncrementPeriod = period;
    }


    public void setUnit(MeasurementCategory category, Unit unit) {
        mUnits.set(category.value(), unit);
    }


    public Unit getUnit(MeasurementCategory category) {
        return mUnits.get(category.value());
    }


    public MeasurementCategory getCategoryToIncrement(){
        return mCategoryToIncrement;
    }


    public int getNumTrackedMeasurements(){
        return mTrackedMeasurementCategories.size();
    }


    public boolean isTracked(MeasurementCategory category) {
        return mTrackedMeasurementCategories.contains(category);
    }

    // Gets the initial measurement value for the given category
    public float getInitialMeasurementValue(MeasurementCategory category){
        MeasurementData initialData = mInitialMeasurementsSet.getMeasurementData(category);
        if (initialData != null) {
            return initialData.getMeasurement();
        }
        else {
            return category.getDefaultMeasurement();
        }
    }

    /**
     * Sets the initial value of a measurement category for this exercise (reps, weight, distance, or time).
     * When the first Session of this exercise is created, the first set will have these initial values
     * When setting the time value, input the amount of time in seconds.
     *
     * @param data The initial MeasurementData to add to the initial measurements
     */
    public void addInitialMeasurementData(MeasurementData data) {
        mInitialMeasurementsSet.addMeasurement(data);
    }

    // Keep tracked measurement category list sorted as categories are added (max size 4)
    public void trackNewMeasurementCategory(MeasurementCategory category){
        if (mTrackedMeasurementCategories.size() == 0)
            mTrackedMeasurementCategories.add(category);
        else {
            for (MeasurementCategory trackedCategory: mTrackedMeasurementCategories){
                if (trackedCategory == category)
                    return;
            }
            int i = 0;
            while ((i < mTrackedMeasurementCategories.size()) &&
                    (mTrackedMeasurementCategories.get(i).value() < category.value()))
                i++;
            mTrackedMeasurementCategories.add(i, category);
        }
    }


    // Remove a measurement from the tracked measurements list
    public void unTrackMeasurementCategory(MeasurementCategory deleteCategory){
        mTrackedMeasurementCategories.remove(deleteCategory);
    }

    public ExerciseSession getMostRecentExerciseSession() {
        return mMostRecentExerciseSession;
    }

    public void setMostRecentExerciseSession(ExerciseSession session) {
        mMostRecentExerciseSession = session;
    }

    public Set generateInitialSet() {
        Set set = new Set();
        for (MeasurementCategory category: mTrackedMeasurementCategories) {
            MeasurementData data = new MeasurementData(
                    category,
                    mInitialMeasurementsSet.getMeasurementData(category).getMeasurement(),
                    this.getUnit(category)
            );
            set.addMeasurement(data);
        }
        return set;
    }

    // Make a new Exercise Session based off of previous exercise session
    public ExerciseSession createNewExerciseSession(){
        ExerciseSession newSession = new ExerciseSession(this, true);
        mMostRecentExerciseSession = newSession;
        return newSession;
    }
}