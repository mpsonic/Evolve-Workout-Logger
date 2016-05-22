package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.RoutineSessionExercisesFragment;

public class ActiveRoutineSession extends AppCompatActivity
        implements RoutineSessionExercisesFragment.OnFragmentInteractionListener{

    private long mRoutineId;
    private Routine mRoutine;
    private RoutineSession mRoutineSession;
    private static final String TAG = ActiveRoutineSession.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Store the routine id so that it can be retrieved from the database
        Intent intent = getIntent();
        mRoutineId = intent.getLongExtra(DatabaseHelper.ROUTINE_ID_NAME, -1);

        // Get the routine and create a new routine session
        DatabaseHelper db = new DatabaseHelper(this);
        mRoutine = db.getRoutine(mRoutineId);
        mRoutine.createNewRoutineSession();
        mRoutineSession = mRoutine.getCurrentRoutineSession();

        setContentView(R.layout.activity_active_routine_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Called by RoutineSessionExercisesFragment in its onAttach method
    @Override
    public void setFragmentRoutineSession() {
        Log.d(TAG, "setFragmentRoutineSession");
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoutineSessionExercisesFragment fragment = (RoutineSessionExercisesFragment)
                fragmentManager.findFragmentById(R.id.fragment_routine_session_exercises);
        fragment.setRoutineSession(mRoutineSession);
    }

    // Called when the user touches one of the exercise sessions in the fragment RecyclerView
    @Override
    public void exerciseSessionSelected(int position) {
        Log.d(TAG, "exerciseSessionSelected");
        Intent i = new Intent(this, ActiveExerciseSession.class);
        startActivity(i);
    }
}
