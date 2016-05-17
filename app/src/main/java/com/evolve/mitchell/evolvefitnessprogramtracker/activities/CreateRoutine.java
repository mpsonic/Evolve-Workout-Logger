package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.RoutineExercisesFragment;

public class CreateRoutine extends AppCompatActivity
        implements RoutineExercisesFragment.OnFragmentInteractionListener {

    private Routine mRoutine;
    private long mRoutineId;
    private String mTitle;
    private String mDescription;
    private EditText mEditTitle;
    private EditText mEditDescription;
    private RoutineExercisesFragment mFragment;
    private FloatingActionButton fab;

    private static final String TITLE_STRING = "Title";
    private static final String DESCRIPTION_STRING = "Description";
    private static final String TAG = "CreateRoutine-A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistRoutineAndFinish();
            }
        });

        mEditTitle = (EditText) findViewById(R.id.edit_title);
        mEditDescription = (EditText) findViewById(R.id.edit_description);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Load the routine that is currently being created
        if (savedInstanceState != null) {
            mRoutineId = savedInstanceState.getLong(DatabaseHelper.ROUTINE_ID_NAME, -1);
            if (mRoutineId != -1) {
                mRoutine = databaseHelper.getRoutine(mRoutineId);
                mTitle = savedInstanceState.getString(TITLE_STRING);
                mDescription = savedInstanceState.getString(DESCRIPTION_STRING);
                mEditTitle.setText(mTitle);
                mEditDescription.setText(mDescription);
            }
        } else {
            mRoutine = new Routine();
            //mRoutineId = databaseHelper.addRoutine(mRoutine, false);
        }

        /*// Add an exercise to the routine if another activity specifies it
        Intent intent = getIntent();
        long exerciseId = intent.getLongExtra(DatabaseHelper.EXERCISE_ID_NAME, -1);
        if (exerciseId != -1) {
            Exercise exercise = databaseHelper.getExercise(exerciseId);
            mRoutine.addExercise(exercise);
            databaseHelper.updateRoutine(mRoutine);
        }*/

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (RoutineExercisesFragment) fm.findFragmentById(R.id.fragment_create_routine);
        mFragment.setRoutine(mRoutine);

        if (mRoutine.getNumExercises() == 0) {
            fab.hide();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        DatabaseHelper db = new DatabaseHelper(this);
        db.updateRoutine(mRoutine);
        db.close();
        outState.putLong(DatabaseHelper.ROUTINE_ID_NAME, mRoutineId);
        outState.putString(TITLE_STRING, mEditTitle.getText().toString());
        outState.putString(DESCRIPTION_STRING, mEditDescription.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void exerciseSelected(int position) {
        Log.d(TAG, "exerciseSelected()");
        //TODO: do something when exercise selected
    }



    public void handleButtonClick(View view) {
        Log.d(TAG, "handleButtonClick()");
        Button button = (Button) view;
        switch (button.getId()) {
            case R.id.button_add_exercise_to_routine:
                addNewExercise();
        }
    }

    public void addNewExercise() {
        Log.d(TAG, "addNewExercise()");
        Intent addExercise = new Intent(this, AddExercise.class);
        startActivityForResult(addExercise, ResponseCodes.NEW_EXERCISE.getValue());
    }

    private void persistRoutineAndFinish() {
        Log.d(TAG, "persistRoutineAndFinish()");
        Intent returnIntent = new Intent();
        long routineId = persistRoutine();
        returnIntent.putExtra(DatabaseHelper.ROUTINE_ID_NAME, routineId);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private long persistRoutine() {
        Log.d(TAG, "persistRoutine");

        EditText titleEdit = (EditText) findViewById(R.id.edit_title);
        EditText descriptionEdit = (EditText) findViewById(R.id.edit_description);
        assert titleEdit != null;
        assert descriptionEdit != null;

        String title = titleEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        mRoutine.setName(title);
        mRoutine.setDescription(description);

        DatabaseHelper db = new DatabaseHelper(this);
        long id = db.addRoutine(mRoutine, true);
        db.close();
        return id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                // Add selected exercise to routine
                long exerciseId = data.getLongExtra(DatabaseHelper.EXERCISE_ID_NAME, -1);
                DatabaseHelper db = new DatabaseHelper(this);
                Exercise exercise = db.getExercise(exerciseId);
                db.close();
                mRoutine.addExercise(exercise);
                mFragment.refresh();
                fab.show();
            }
        }

    }
}
