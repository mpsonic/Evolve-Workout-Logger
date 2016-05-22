package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;

public class ActiveExerciseSession extends AppCompatActivity {

    private long mExerciseId;
    private Exercise mExercise;
    private ExerciseSession mExerciseSession;
    private static final String TAG = ActiveExerciseSession.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        /*// Store the routine id so that it can be retrieved from the database
        Intent intent = getIntent();
        mExerciseSessionId = intent.getLongExtra(DatabaseHelper.EXERCISE_SESSON_ID_NAME, -1);

        // Get the routine and create a new routine session
        DatabaseHelper db = new DatabaseHelper(this);
        mRoutine = db.getRoutine(mRoutineId);
        mRoutine.createNewRoutineSession();
        mRoutineSession = mRoutine.getCurrentRoutineSession();*/

        setContentView(R.layout.activity_active_routine_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Called by RoutineSessionExercisesFragment in its onAttach method
    /*@Override
    public void setFragmentExerciseSession() {
        Log.d(TAG, "setFragmentRoutineSession");
        FragmentManager fragmentManager = getSupportFragmentManager();
        ExerciseSessionSetsFragment fragment = (ExerciseSessionSetsFragment)
                fragmentManager.findFragmentById(R.id.fragment_exercise_session_sets);
        fragment.setExerciseSession(mExerciseSession);
    }*/
}
