package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;

import java.util.ArrayList;

public class CreateExercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    RelativeLayout contentLayout;
    RelativeLayout unitsLayout;
    RelativeLayout trackedMeasurementsLayout;
    LinearLayout startingMeasurementsLayout;
    RelativeLayout targetIncreaseLayout;
    RelativeLayout increasePerSessionLayout;
    RelativeLayout categoryLayout;

    ArrayList<ArrayList<View>> unitViews;
    ArrayList<String> distanceImperialUnits;
    ArrayList<String> distanceMetricUnits;
    ArrayList<String> exerciseCategories;

    ToggleButton imperialToggle;
    ToggleButton metricToggle;

    FloatingActionButton fab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get content layouts
        contentLayout = (RelativeLayout) findViewById(R.id.createExerciseContent);
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
        distanceImperialUnits = new ArrayList<>(2);
        distanceImperialUnits.add("feet");
        distanceImperialUnits.add("miles");

        distanceMetricUnits = new ArrayList<>(2);
        distanceMetricUnits.add("meters");
        distanceMetricUnits.add("kilometers");

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


        // Initialize starting measurement rows, target toggle buttons, and increase per session views to gone
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
                finish();
            }
        });
        fab.hide();
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
        TableRow row = (TableRow) unitViews.get(category.value()).get(1);
        if (hasMeasurementsTracked()) {
            startingMeasurementsLayout.setVisibility(View.VISIBLE);
            targetIncreaseLayout.setVisibility(View.VISIBLE);
            categoryLayout.setVisibility(View.VISIBLE);
            fab.show();
        }
        else {
            startingMeasurementsLayout.setVisibility(View.GONE);
            targetIncreaseLayout.setVisibility(View.GONE);
            categoryLayout.setVisibility(View.GONE);
            fab.hide();
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

    public void finish() {

    }
}
