package edu.umn.paull011.evolveworkoutlogger.activities;

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

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.fragments.RoutineExercisesFragment;

public class CreateRoutine extends AppCompatActivity
        implements RoutineExercisesFragment.OnFragmentInteractionListener {

    private Routine mRoutine;
    private String mTitle;
    private String mDescription;
    private EditText mEditTitle;
    private EditText mEditDescription;
    private RoutineExercisesFragment mFragment;
    private FloatingActionButton fab;

    private boolean mEditingMode;
    private static final String TITLE_STRING = "Title";
    private static final String DESCRIPTION_STRING = "Description";
    private static final String TAG = CreateRoutine.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        mEditingMode = getIntent().getBooleanExtra("Edit", false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistRoutineAndFinish();
            }
        });

        mEditTitle = (EditText) findViewById(R.id.edit_title);
        mEditDescription = (EditText) findViewById(R.id.edit_description);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);

        // Load the routine that is currently being created

        if (savedInstanceState != null) {
            char[] temp = savedInstanceState.getCharArray(DatabaseHelper.KEY_ROUTINE_NAME);
            if (temp != null) {
                String routineName = String.valueOf(temp);
                mRoutine = databaseHelper.getRoutine(routineName);
                mTitle = savedInstanceState.getString(TITLE_STRING);
                mDescription = savedInstanceState.getString(DESCRIPTION_STRING);
            }
        }
        else {
            if (mEditingMode) {
                String routineName = getIntent().getStringExtra(DatabaseHelper.KEY_ROUTINE_NAME);
                mRoutine = databaseHelper.getRoutine(routineName);
                mTitle = mRoutine.getName();
                mDescription = mRoutine.getDescription();
            }
            else {
                mRoutine = new Routine();
            }
        }
        mEditTitle.setText(mTitle);
        mEditDescription.setText(mDescription);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (RoutineExercisesFragment) fm.findFragmentById(R.id.fragment_create_routine);
        mFragment.setRoutine(mRoutine);

        if (mRoutine.getNumExercises() == 0) {
            fab.hide();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mEditingMode) {
            toolbar.setTitle("Edit Routine");
        }
        else {
            toolbar.setTitle("Create Routine");
        }
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        extractRoutineDataFromActivity();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.updateRoutine(mRoutine);
        db.close();
        outState.putString(DatabaseHelper.KEY_ROUTINE_NAME, mRoutine.getName());
        outState.putString(TITLE_STRING, mEditTitle.getText().toString());
        outState.putString(DESCRIPTION_STRING, mEditDescription.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void exerciseSelected(String exerciseName) {
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
        boolean routineSaved = persistRoutine();
        if (routineSaved){
            returnIntent.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, mRoutine.getName());
            setResult(Activity.RESULT_OK, returnIntent);
        }
        finish();
    }

    private void extractRoutineDataFromActivity() {
        EditText titleEdit = (EditText) findViewById(R.id.edit_title);
        EditText descriptionEdit = (EditText) findViewById(R.id.edit_description);
        assert titleEdit != null;
        assert descriptionEdit != null;

        String title = titleEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        mRoutine.setName(title);
        mRoutine.setDescription(description);
    }

    private boolean persistRoutine() {
        Log.d(TAG, "persistRoutine");

        extractRoutineDataFromActivity();

        String routineName;
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        if (mEditingMode && mTitle != mRoutine.getName()) {
            routineName = db.replaceRoutine(mTitle, mRoutine);
        }
        else {
            routineName = db.insertRoutine(mRoutine, true);
        }
        db.close();
        return routineName.equals(mRoutine.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                // Add selected exercise to routine
                String exerciseName = data.getStringExtra(DatabaseHelper.KEY_EXERCISE_NAME);
                DatabaseHelper db = DatabaseHelper.getInstance(this);
                Exercise exercise = db.getExercise(exerciseName);
                mRoutine.addExercise(exercise);
                db.insertRoutineExercise(
                        mRoutine.getName(),
                        exercise.getName(),
                        mRoutine.getNumExercises() - 1
                );
                mFragment.refresh();
                fab.show();
            }
        }
    }
}
