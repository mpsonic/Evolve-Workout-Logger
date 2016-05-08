package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.RoutineExercisesFragment;

public class CreateRoutine extends AppCompatActivity
        implements RoutineExercisesFragment.OnFragmentInteractionListener {

    private Routine mRoutine;
    private long mRoutineId;

    public static final String ROUTINE_ID = "RoutineId";
    public static final String ADD_EXERCISE_FLAG = "AddExercise";
    public static final String EXERCISE_ID = "ExerciseId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Load the routine that is currently being created
        if (savedInstanceState != null) {
            mRoutineId = savedInstanceState.getLong(ROUTINE_ID, -1);
            if (mRoutineId != -1) {
                mRoutine = databaseHelper.getRoutine(mRoutineId);
            }
        } else {
            mRoutine = new Routine();
            //mRoutineId = databaseHelper.addRoutine(mRoutine);
        }

        // Add an exercise to the routine if another activity specifies it
        Intent intent = getIntent();
        if (intent.getBooleanExtra(ADD_EXERCISE_FLAG, false)) {
            int exerciseId = intent.getIntExtra(EXERCISE_ID, -1);
            if (exerciseId != -1) {
                Exercise exercise = databaseHelper.getExercise(exerciseId);
                mRoutine.addExercise(exercise);
                databaseHelper.updateRoutine(mRoutine);
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        RoutineExercisesFragment reFragment = (RoutineExercisesFragment) fm.findFragmentById(R.id.fragment_create_routine);
        reFragment.setRoutine(mRoutine);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ROUTINE_ID, mRoutineId);
    }

    @Override
    public void exerciseSelected(int position) {
        //TODO: do something when exercise selected
    }

    public void addNewExercise() {
        Intent createExercise = new Intent(this, CreateExercise.class);
        createExercise.putExtra(ROUTINE_ID, mRoutineId);
        startActivity(createExercise);
    }

    public void handleButtonClick(View view) {
        Button button = (Button) view;
        switch (button.getId()) {
            case R.id.button_add_exercise_to_routine:
                addNewExercise();
        }
    }

}
