package edu.umn.paull011.evolveworkoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.components.ButtonEditText;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementData;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Unit;

public class CreateExercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Boolean mExerciseCreated = false;

    private ScrollView contentLayout;
    private RelativeLayout unitsLayout;
    private RelativeLayout trackedMeasurementsLayout;
    private LinearLayout startingMeasurementsLayout;
    private RelativeLayout targetIncreaseLayout;
    private RelativeLayout increasePerSessionLayout;
    private RelativeLayout categoryLayout;

    private ArrayList<ArrayList<View>> unitViews;
    private ArrayList<String> distanceImperialUnits;
    private ArrayList<String> distanceMetricUnits;
    private ArrayList<String> exerciseCategories;

    private ToggleButton imperialToggle;
    private ToggleButton metricToggle;

    private FloatingActionButton fab;

    private boolean mEditingMode;
    private String mExerciseName;
    private static final String TAG = CreateExercise.class.getSimpleName();

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        // Get content layouts
        contentLayout = (ScrollView) findViewById(R.id.createExerciseContent);

        assert contentLayout != null;
        unitsLayout = (RelativeLayout) contentLayout.findViewById(R.id.unitsLayout);
        trackedMeasurementsLayout = (RelativeLayout) contentLayout.findViewById(R.id.trackedMeasurementsLayout);
        startingMeasurementsLayout = (LinearLayout) contentLayout.findViewById(R.id.startingMeasurementsLayout);
        targetIncreaseLayout = (RelativeLayout) contentLayout.findViewById(R.id.targetIncreaseLayout);
        increasePerSessionLayout = (RelativeLayout) contentLayout.findViewById(R.id.increasePerSessionLayout);
        categoryLayout = (RelativeLayout) contentLayout.findViewById(R.id.categoryLayout);

        // Get unit views
        ArrayList<View> repsViews = new ArrayList<>(4);
        ArrayList<View> weightViews = new ArrayList<>(4);
        ArrayList<View> distanceViews = new ArrayList<>(4);
        ArrayList<View> timeViews = new ArrayList<>(4);

        repsViews.add(trackedMeasurementsLayout.findViewById(R.id.repsToggleButton));
        repsViews.add(contentLayout.findViewById(R.id.repsRow));
        repsViews.add(targetIncreaseLayout.findViewById(R.id.repsTargetToggleButton));
        repsViews.add(increasePerSessionLayout.findViewById(R.id.repsIncreaseLayout));
        weightViews.add(trackedMeasurementsLayout.findViewById(R.id.weightToggleButton));
        weightViews.add(contentLayout.findViewById(R.id.weightRow));
        weightViews.add(targetIncreaseLayout.findViewById(R.id.weightTargetToggleButton));
        weightViews.add(increasePerSessionLayout.findViewById(R.id.weightIncreaseLayout));
        distanceViews.add(trackedMeasurementsLayout.findViewById(R.id.distanceToggleButton));
        distanceViews.add(contentLayout.findViewById(R.id.distanceRow));
        distanceViews.add(targetIncreaseLayout.findViewById(R.id.distanceTargetToggleButton));
        distanceViews.add(increasePerSessionLayout.findViewById(R.id.distanceIncreaseLayout));
        timeViews.add(trackedMeasurementsLayout.findViewById(R.id.timeToggleButton));
        timeViews.add(contentLayout.findViewById(R.id.timeRow));
        timeViews.add(targetIncreaseLayout.findViewById(R.id.timeTargetToggleButton));
        timeViews.add(increasePerSessionLayout.findViewById(R.id.timeIncreaseLayout));

        unitViews = new ArrayList<>(4);
        unitViews.add(repsViews);
        unitViews.add(weightViews);
        unitViews.add(distanceViews);
        unitViews.add(timeViews);

        // Get the ToggleButtons
        imperialToggle = (ToggleButton) unitsLayout.findViewById(R.id.imperialToggleButton);
        metricToggle = (ToggleButton) unitsLayout.findViewById(R.id.metricToggleButton);

        // Set up spinners
        // TODO: Set up a unit arrayAdapter
        distanceImperialUnits = new ArrayList<>(2);
        distanceImperialUnits.add("ft");
        distanceImperialUnits.add("mi");

        distanceMetricUnits = new ArrayList<>(2);
        distanceMetricUnits.add("m");
        distanceMetricUnits.add("km");

        // TODO: Set up a category arrayAdapter
        exerciseCategories = new ArrayList<>();
        exerciseCategories.add("Arms");
        exerciseCategories.add("Abs");
        exerciseCategories.add("Back");
        exerciseCategories.add("Chest");
        exerciseCategories.add("Legs");
        exerciseCategories.add("Cardio");

        Spinner dSpinner = (Spinner) findViewById(R.id.distanceUnitSpinner);
        Spinner dIncreaseSpinner = (Spinner) findViewById(R.id.distanceIncreaseUnitSpinner);
        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        assert dSpinner != null;
        assert dIncreaseSpinner != null;
        assert categorySpinner != null;

        dSpinner.setOnItemSelectedListener(this);
        dIncreaseSpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> distanceArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceImperialUnits);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exerciseCategories);
        distanceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dSpinner.setAdapter(distanceArrayAdapter);
        dIncreaseSpinner.setAdapter(distanceArrayAdapter);
        categorySpinner.setAdapter(categoryArrayAdapter);
        dSpinner.setSelection(0);
        dIncreaseSpinner.setSelection(0);
        categorySpinner.setSelection(0);


        // Initialize Form
        for (int unitViewIndex = 1; unitViewIndex<=3; unitViewIndex++) {
            for (int unit = 0; unit <=3; unit++) {
                unitViews.get(unit).get(unitViewIndex).setVisibility(View.GONE);
            }
        }

        startingMeasurementsLayout.setVisibility(View.GONE);
        targetIncreaseLayout.setVisibility(View.GONE);
        increasePerSessionLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExerciseCreated = true;
                persistExerciseAndFinish();
            }
        });
        fab.hide();

        Intent intent = getIntent();
        if (intent.getBooleanExtra("Edit", false)) { // Editing an existing exercise
            mEditingMode = true;
            mExerciseName = intent.getStringExtra(DatabaseHelper.KEY_EXERCISE_NAME);
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            Exercise exercise = db.getExercise(mExerciseName);

            // Populate title edit text
            EditText titleEdit = (EditText) findViewById(R.id.edit_title);
            titleEdit.setText(mExerciseName);

            // Set unit system
            if (exercise.isImperial()) {
                handleButtonClick(imperialToggle);
            }
            else {
                handleButtonClick(metricToggle);
            }

            // Set tracked measurements and starting measurements
            for (MeasurementCategory category: MeasurementCategory.values()) {
                if (exercise.isTracked(category)) {
                    // Click tracked toggle button
                    ToggleButton trackedToggle = (ToggleButton) unitViews.get(category.value()).get(0);
                    trackedToggle.setChecked(true);
                    handleButtonClick(trackedToggle);

                    // Set up starting measurement views
                    Unit unit = exercise.getUnit(category);
                    String unitDisplay = unit.getDisplayName();
                    ButtonEditText bet;
                    Float initialMeasurement = exercise.getInitialMeasurementValue(category);
                    switch (category) {
                        case REPS:
                            bet = (ButtonEditText) findViewById(R.id.bet_reps_starting_measurement);
                            bet.setNumber(initialMeasurement);
                            break;
                        case WEIGHT:
                            bet = (ButtonEditText) findViewById(R.id.bet_weight_starting_measurement);
                            bet.setNumber(initialMeasurement);
                            TextView weightUnit = (TextView) findViewById(R.id.weightUnitText);
                            weightUnit.setText(unitDisplay);
                            break;
                        case DISTANCE:
                            bet = (ButtonEditText) findViewById(R.id.bet_distance_starting_measurement);
                            bet.setNumber(initialMeasurement);
                            dSpinner.setSelection(distanceArrayAdapter.getPosition(unitDisplay));
                            break;
                        case TIME:
                            bet = (ButtonEditText) findViewById(R.id.bet_time_hours_starting_measurement);
                            bet.setNumber(initialMeasurement/3600);
                            bet = (ButtonEditText) findViewById(R.id.bet_time_minutes_starting_measurement);
                            bet.setNumber((initialMeasurement % 3600) / 60);
                            bet = (ButtonEditText) findViewById(R.id.bet_time_seconds_starting_measurement);
                            bet.setNumber(initialMeasurement % 60);
                            break;
                    }

                    if (exercise.getCategoryToIncrement() == category) {
                        // Click target toggle button
                        ToggleButton incrementToggle = (ToggleButton)
                                unitViews.get(category.value()).get(2);
                        incrementToggle.setChecked(true);
                        handleButtonClick(incrementToggle);


                        // Set up measurement increment views
                        float increment = exercise.getIncrement();
                        switch (category) {
                            case REPS:
                                bet = (ButtonEditText) findViewById(R.id.create_exercise_bet_increase_reps);
                                bet.setNumber(increment);
                                break;
                            case WEIGHT:
                                bet = (ButtonEditText) findViewById(R.id.create_exercise_bet_increase_weight);
                                bet.setNumber(increment);
                                TextView weightUnit = (TextView) findViewById(R.id.weightIncreaseUnitText);
                                weightUnit.setText(unitDisplay);
                                break;
                            case DISTANCE:
                                bet = (ButtonEditText) findViewById(R.id.create_exercise_bet_increase_distance);
                                bet.setNumber(increment);
                                dIncreaseSpinner.setSelection(distanceArrayAdapter.getPosition(unitDisplay));
                                break;
                            case TIME:
                                bet = (ButtonEditText) findViewById(R.id.create_exercise_bet_increase_time_minutes);
                                bet.setNumber((int) increment / 60);
                                bet = (ButtonEditText) findViewById(R.id.create_exercise_bet_increase_time_seconds);
                                bet.setNumber((int) increment % 60);
                        }
                    }
                }
            }

            // Set the category spinner
            String category = exercise.getExerciseCategory();
            categorySpinner.setSelection(categoryArrayAdapter.getPosition(category));
        }
        else { // Creating a new exercise
            mEditingMode = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mEditingMode) {
            toolbar.setTitle("Edit Exercise");
        }
        else {
            toolbar.setTitle("Create Exercise");
        }
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mExerciseCreated) {
            persistExercise(false);
        }
    }

    private boolean hasMeasurementsTracked() {
        ToggleButton repsToggle = (ToggleButton) unitViews.get(0).get(0);
        ToggleButton weightToggle = (ToggleButton) unitViews.get(1).get(0);
        ToggleButton distanceToggle = (ToggleButton) unitViews.get(2).get(0);
        ToggleButton timeToggle = (ToggleButton) unitViews.get(3).get(0);
        boolean reps = repsToggle.isChecked();
        boolean weight = weightToggle.isChecked();
        boolean distance = distanceToggle.isChecked();
        boolean time = timeToggle.isChecked();
        return reps || weight || distance || time;
    }

    private boolean hasMeasurementTarget() {
        ToggleButton repsToggle = (ToggleButton) unitViews.get(0).get(2);
        ToggleButton weightToggle = (ToggleButton) unitViews.get(1).get(2);
        ToggleButton distanceToggle = (ToggleButton) unitViews.get(2).get(2);
        ToggleButton timeToggle = (ToggleButton) unitViews.get(3).get(2);
        boolean reps = repsToggle.isChecked();
        boolean weight = weightToggle.isChecked();
        boolean distance = distanceToggle.isChecked();
        boolean time = timeToggle.isChecked();
        return reps || weight || distance || time;
    }

    private void trackedMeasurementToggleClicked(MeasurementCategory category) {
        ToggleButton trackedToggle = (ToggleButton) unitViews.get(category.value()).get(0);
        ToggleButton targetToggle = (ToggleButton) unitViews.get(category.value()).get(2);
        LinearLayout row = (LinearLayout) unitViews.get(category.value()).get(1);
        if (hasMeasurementsTracked()) {
            if (!mEditingMode) {
                startingMeasurementsLayout.setVisibility(View.VISIBLE);
            }
            targetIncreaseLayout.setVisibility(View.VISIBLE);
            categoryLayout.setVisibility(View.VISIBLE);
            showFab();
        }
        else {
            startingMeasurementsLayout.setVisibility(View.GONE);
            targetIncreaseLayout.setVisibility(View.GONE);
            categoryLayout.setVisibility(View.GONE);
            hideFab();
        }
        if (trackedToggle.isChecked()) {
            targetToggle.setVisibility(View.VISIBLE);
            row.setVisibility(View.VISIBLE);
        }
        else {
            targetToggle.setVisibility(View.GONE);
            if (targetToggle.isChecked()) {
                targetToggle.setChecked(false);
                increasePerSessionLayout.setVisibility(View.GONE);
            }
            row.setVisibility(View.GONE);
        }
    }

    private void targetToggleClicked(MeasurementCategory category){
        ToggleButton targetToggle = (ToggleButton) unitViews.get(category.value()).get(2);
        LinearLayout increaseUnitLayout = (LinearLayout) unitViews.get(category.value()).get(3);
        ToggleButton otherToggle;
        LinearLayout otherIncreaseLayout;
        if (targetToggle.isChecked()) {
            for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
                if (category != unitCategory) {
                    otherToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(2);
                    otherIncreaseLayout = (LinearLayout) unitViews.get(unitCategory.value()).get(3);
                    otherToggle.setChecked(false);
                    otherIncreaseLayout.setVisibility(View.GONE);
                }
            }
            increaseUnitLayout.setVisibility(View.VISIBLE);
            increasePerSessionLayout.setVisibility(View.VISIBLE);
        }
        else {
            increaseUnitLayout.setVisibility(View.GONE);
            increasePerSessionLayout.setVisibility(View.GONE);
        }
    }

    private void setUnits(boolean imperial){
        TextView weightUnit = (TextView) findViewById(R.id.weightUnitText);
        Spinner dSpinner = (Spinner) findViewById(R.id.distanceUnitSpinner);
        TextView weightIncreaseUnit = (TextView) findViewById(R.id.weightIncreaseUnitText);
        Spinner dIncreaseSpinner = (Spinner) findViewById(R.id.distanceIncreaseUnitSpinner);
        assert weightUnit != null;
        assert dSpinner != null;
        assert weightIncreaseUnit != null;
        assert dIncreaseSpinner != null;

        if (imperial) {
            weightUnit.setText(R.string.weight_imperial_unit);
            weightIncreaseUnit.setText(R.string.weight_imperial_unit);
            ArrayAdapter<String> dSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceImperialUnits);
            dSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dSpinner.setAdapter(dSpinnerAdapter);
            dIncreaseSpinner.setAdapter(dSpinnerAdapter);
            dSpinner.setSelection(0);
            dIncreaseSpinner.setSelection(0);
        }
        else {
            weightUnit.setText(R.string.weight_metric_unit);
            weightIncreaseUnit.setText(R.string.weight_metric_unit);
            ArrayAdapter<String> dSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceMetricUnits);
            dSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dSpinner.setAdapter(dSpinnerAdapter);
            dIncreaseSpinner.setAdapter(dSpinnerAdapter);
            dSpinner.setSelection(0);
            dIncreaseSpinner.setSelection(0);
        }
    }

    public void handleButtonClick(View view) {
        ToggleButton tb = (ToggleButton) view;
        switch (tb.getId()) {
            case R.id.imperialToggleButton:
                metricToggle.setChecked(!imperialToggle.isChecked());
                setUnits(imperialToggle.isChecked());
                break;
            case R.id.metricToggleButton:
                imperialToggle.setChecked(!metricToggle.isChecked());
                setUnits(imperialToggle.isChecked());
                break;
            case R.id.repsToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.REPS);
                break;
            case R.id.weightToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.WEIGHT);
                break;
            case R.id.distanceToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.DISTANCE);
                break;
            case R.id.timeToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.TIME);
                break;
            case R.id.repsTargetToggleButton:
                targetToggleClicked(MeasurementCategory.REPS);
                break;
            case R.id.weightTargetToggleButton:
                targetToggleClicked(MeasurementCategory.WEIGHT);
                break;
            case R.id.distanceTargetToggleButton:
                targetToggleClicked(MeasurementCategory.DISTANCE);
                break;
            case R.id.timeTargetToggleButton:
                targetToggleClicked(MeasurementCategory.TIME);
                break;
            default:
                break;
        }
    }

    private Exercise createExerciseFromFormData() {
        Exercise exercise = new Exercise();

        EditText titleEdit = (EditText) findViewById(R.id.edit_title);
        assert titleEdit != null;
        exercise.setName(titleEdit.getText().toString());

        if (imperialToggle.isChecked()) {
            exercise.setImperialUnits();
        }
        else {
            exercise.setMetricUnits();
        }

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        String category = categorySpinner.getSelectedItem().toString();
        exercise.setExerciseCategory(category);

        for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
            ToggleButton trackedToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(0);
            if (trackedToggle.isChecked()) {
                exercise.trackNewMeasurementCategory(unitCategory);
                switch (unitCategory) {
                    case REPS:
                        ButtonEditText repsBET = (ButtonEditText) findViewById(R.id.bet_reps_starting_measurement);
                        assert repsBET != null;
                        int startingReps = (int) repsBET.getNumber();
                        try {
                            exercise.addInitialMeasurementData(
                                    new MeasurementData(
                                            MeasurementCategory.REPS,
                                            startingReps
                                    )
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case WEIGHT:
                        ButtonEditText weightBET = (ButtonEditText) findViewById(R.id.bet_weight_starting_measurement);
                        assert weightBET != null;
                        float startingWeight = (int) weightBET.getNumber();
                        Unit weightUnit = MeasurementCategory.WEIGHT.getDefaultUnit(exercise.isImperial());
                        try {
                            exercise.addInitialMeasurementData(
                                    new MeasurementData(
                                            MeasurementCategory.WEIGHT,
                                            startingWeight

                                    )
                            );
                            exercise.setUnit(MeasurementCategory.WEIGHT, weightUnit);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case DISTANCE:
                        ButtonEditText distanceBET = (ButtonEditText) findViewById(R.id.bet_distance_starting_measurement);
                        Spinner distanceUnitSpinner = (Spinner) findViewById(R.id.distanceUnitSpinner);
                        Unit distanceUnit = Unit.getFromName(distanceUnitSpinner.getSelectedItem().toString());
                        assert distanceBET != null;
                        assert distanceUnit != null;
                        float startingDistance = (int) distanceBET.getNumber();
                        try {
                            exercise.addInitialMeasurementData(
                                    new MeasurementData(
                                            MeasurementCategory.DISTANCE,
                                            startingDistance
                                    )
                            );
                            exercise.setUnit(MeasurementCategory.DISTANCE, distanceUnit);
                            //exercise.setTrackedMeasurementUnit(MeasurementCategory.DISTANCE, Unit.KILOMETERS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case TIME:
                        ButtonEditText hoursBET = (ButtonEditText) findViewById(R.id.bet_time_hours_starting_measurement);
                        ButtonEditText minutesBET = (ButtonEditText) findViewById(R.id.bet_time_minutes_starting_measurement);
                        ButtonEditText secondsBET = (ButtonEditText) findViewById(R.id.bet_time_seconds_starting_measurement);
                        assert hoursBET != null;
                        assert minutesBET != null;
                        assert secondsBET != null;
                        float hours = (int) hoursBET.getNumber();
                        float minutes = (int) minutesBET.getNumber();
                        float seconds = (int) secondsBET.getNumber();
                        float timeInSeconds = 3600*hours + 60*minutes + seconds;
                        try {
                            exercise.addInitialMeasurementData(
                                    new MeasurementData(
                                            MeasurementCategory.TIME,
                                            timeInSeconds
                                    )
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
        float increment = 0;
        for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
            ToggleButton targetToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(2);
            if (targetToggle.isChecked()) {
                switch (unitCategory) {
                    case REPS:
                        ButtonEditText repsIncreaseBET = (ButtonEditText)
                                findViewById(R.id.create_exercise_bet_increase_reps);
                        assert repsIncreaseBET != null;
                        increment = repsIncreaseBET.getNumber();
                        break;
                    case WEIGHT:
                        ButtonEditText weightIncreaseBET = (ButtonEditText)
                                findViewById(R.id.create_exercise_bet_increase_weight);
                        assert weightIncreaseBET != null;
                        increment = weightIncreaseBET.getNumber();
                        break;
                    case DISTANCE:
                        ButtonEditText distanceIncreaseBET = (ButtonEditText)
                                findViewById(R.id.create_exercise_bet_increase_distance);
                        assert distanceIncreaseBET != null;
                        increment = distanceIncreaseBET.getNumber();
                        break;
                    case TIME:
                        ButtonEditText minutesBET = (ButtonEditText)
                                findViewById(R.id.create_exercise_bet_increase_time_minutes);
                        ButtonEditText secondsBET = (ButtonEditText)
                                findViewById(R.id.create_exercise_bet_increase_time_seconds);
                        assert minutesBET != null;
                        assert secondsBET != null;
                        float minutes = minutesBET.getNumber();
                        float seconds = secondsBET.getNumber();
                        increment = 60*minutes + seconds;
                        break;
                }
                exercise.setIncrement(increment);
                exercise.setMeasurementCategoryToIncrement(unitCategory);
            }
        }
        return exercise;
    }

    private String persistExercise(boolean permanent) {
        Exercise exercise = createExerciseFromFormData();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        if (mEditingMode && !(mExerciseName.equals(exercise.getName()))) {
            db.replaceExercise(mExerciseName, exercise);
        }
        else {
            db.insertExercise(exercise, permanent);
        }
        return exercise.getName();
    }

    public void persistExerciseAndFinish() {
        String exerciseName = persistExercise(true);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, exerciseName);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void hideFab() {
        /*CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);*/
        fab.setVisibility(View.GONE);
    }

    private void showFab() {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(new FloatingActionButton.Behavior());
        p.setAnchorId(R.id.app_bar_layout);
        fab.setLayoutParams(p);
        fab.show();
    }

    // Removes focus from EditTexts when any other area of the activity is touched
    // Doesn't allow ButtonEditText buttons to work
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
