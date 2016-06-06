package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.ExerciseSessionSetsFragment;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RoutineSessionDataHolder;

public class ActiveExerciseSession extends AppCompatActivity
    implements ExerciseSessionSetsFragment.OnFragmentInteractionListener{

    private RoutineSessionDataHolder dataHolder = RoutineSessionDataHolder.getInstance();
    private Exercise mExercise;
    private ExerciseSession mExerciseSession;
    private int mExercisePosition;
    private ExerciseSessionSetsFragment mSetsFragment;


    private int mCurrentSetIndex = 0;
    private static final String TAG = ActiveExerciseSession.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mExercisePosition = intent.getIntExtra(DatabaseHelper.KEY_POSITION, -1);
        mExercise = dataHolder.getRoutine().getExercise(mExercisePosition);
        mExerciseSession = dataHolder.getRoutineSession().getExerciseSession(mExercisePosition);
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        RoutineSession routineSession = dataHolder.getRoutineSession();

        setContentView(R.layout.activity_active_exercise_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mExercise.getName() + "  (" +
                String.valueOf(mExercisePosition + 1) + "/" + routineSession.getExerciseCount() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addSetButton = (Button) findViewById(R.id.button_add_set);
        Button nextSetButton = (Button) findViewById(R.id.button_next_set);
        assert addSetButton != null;
        assert nextSetButton != null;

        addSetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(v);
                    }
                }
        );

        nextSetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(v);
                    }
                }
        );
    }

    private void handleButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_add_set:
                mExerciseSession.generateNewSet();
                mSetsFragment.refreshSetAdded();
                mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
                break;
            case R.id.button_next_set:
                mExerciseSession.completeCurrentSetAndMoveToNext();
                mSetsFragment.refreshNextSet(mCurrentSetIndex);
                mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_active_exercise_session, menu);
        MenuItem previous = menu.findItem(R.id.previous_exercise);
        MenuItem next = menu.findItem(R.id.next_exercise);
        int numExercises = dataHolder.getRoutineSession().getExerciseCount();
        if (mExercisePosition == 0) {
            previous.setVisible(false);
        }
        if (mExercisePosition == numExercises - 1) {
            next.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.previous_exercise:
                i = new Intent(this, ActiveExerciseSession.class);
                i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition - 1);
                startActivity(i);
                finish();
                return true;
            case R.id.next_exercise:
                i = new Intent(this, ActiveExerciseSession.class);
                i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition + 1);
                startActivity(i);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSelected(int position) {
        Log.d(TAG, "setSelected("+ position +")");
        // color selected row? activate buttonEditText?
    }

    // Called by RoutineSessionExercisesFragment in its onAttach method
    public void setFragmentExerciseSession() {
        Log.d(TAG, "setFragmentRoutineSession");
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSetsFragment = (ExerciseSessionSetsFragment)
                fragmentManager.findFragmentById(R.id.fragment_exercise_session_sets);
        mSetsFragment.setExerciseSession(mExerciseSession);
    }
}