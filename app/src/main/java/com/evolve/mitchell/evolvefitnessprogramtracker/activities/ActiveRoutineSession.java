package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.RoutineSessionExercisesFragment;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.AreYouSureDialog;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RoutineSessionDataHolder;

public class ActiveRoutineSession extends AppCompatActivity
        implements RoutineSessionExercisesFragment.OnFragmentInteractionListener,
        AreYouSureDialog.DialogChooserListener{

    private Routine mRoutine;
    private RoutineSession mRoutineSession;
    private static final String TAG = ActiveRoutineSession.class.getSimpleName();
    private RoutineSessionDataHolder dataHolder = RoutineSessionDataHolder.getInstance();
    private RoutineSessionExercisesFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Store the routine id so that it can be retrieved from the database
        Intent intent = getIntent();
        String routineName = intent.getStringExtra(DatabaseHelper.KEY_ROUTINE_NAME);

        // Get the routine and create a new routine session
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        mRoutine = db.getRoutine(routineName);
        mRoutineSession = mRoutine.createNewRoutineSession();
        dataHolder.setRoutine(mRoutine);
        dataHolder.setRoutineSession(mRoutineSession);
        db.insertRoutineSession(mRoutineSession);

        setContentView(R.layout.activity_active_routine_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mRoutine.getName());
        setSupportActionBar(toolbar);

        /*TextView dateText = (TextView) findViewById(R.id.active_routine_session_date);
        assert dateText != null;
        Date date = mRoutineSession.getDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        dateText.setText(dateFormat.format(date));*/

        Button addExerciseButton = (Button) findViewById(R.id.button_add_exercise_to_routine_session);
        assert addExerciseButton != null;
        addExerciseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewExercise();
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AreYouSureDialog dialog = new AreYouSureDialog();
                        dialog.show(getFragmentManager(), "AreYouSureDialog");
                    }
                }
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFragment.refresh();
    }

    // Called by RoutineSessionExercisesFragment in its onAttach method
    @Override
    public void setFragmentRoutineSession() {
        Log.d(TAG, "setFragmentRoutineSession");
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = (RoutineSessionExercisesFragment)
                fragmentManager.findFragmentById(R.id.fragment_routine_session_exercises);
        mFragment.setRoutineSession(mRoutineSession);
    }

    // Called when the user touches one of the exercise sessions in the fragment RecyclerView
    @Override
    public void exerciseSessionSelected(int position) {
        Log.d(TAG, "exerciseSessionSelected");
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, position);
        startActivity(i);
    }

    public void addNewExercise() {
        Log.d(TAG, "addNewExercise()");
        Intent addExercise = new Intent(this, AddExercise.class);
        startActivityForResult(addExercise, ResponseCodes.NEW_EXERCISE.getValue());
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
                mRoutineSession.addNewExerciseSessionFromExercise(exercise, true);
                db.insertRoutineExercise(
                        mRoutine.getName(),
                        exercise.getName(),
                        mRoutine.getNumExercises() - 1
                );
                mFragment.refresh();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        mRoutineSession.finish();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.insertRoutineSession(mRoutineSession);
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
