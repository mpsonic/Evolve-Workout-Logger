package edu.umn.paull011.evolveworkoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;
import edu.umn.paull011.evolveworkoutlogger.fragments.ExerciseSessionSetsFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineSessionDataHolder;

public class ActiveExerciseSession extends AppCompatActivity
    implements ExerciseSessionSetsFragment.OnFragmentInteractionListener{

    private RoutineSessionDataHolder dataHolder = RoutineSessionDataHolder.getInstance();
    private Exercise mExercise;
    private ExerciseSession mExerciseSession;
    private RoutineSession mRoutineSession;
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
        mRoutineSession = dataHolder.getRoutineSession();
        mExerciseSession = mRoutineSession.getExerciseSession(mExercisePosition);
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        RoutineSession routineSession = dataHolder.getRoutineSession();

        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_active_exercise_session);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mExercise.getName() + "  (" +
                String.valueOf(mExercisePosition + 1) + "/" + routineSession.getExerciseCount() + ")");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleButtonClick(view);
                }
            }
        );

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleButtonClick(view);
                    }
                }
        );
       /* Button addSetButton = (Button) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.button_add_set);
        Button nextSetButton = (Button) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.button_next_set);
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
        );*/
    }

    private void handleButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                if (mExerciseSession.isCompleted()) {
                    if (mExercisePosition == mRoutineSession.getExerciseCount() - 1) {
                        NavUtils.navigateUpFromSameTask(this);
                    }
                    else {
                        moveToNextExerciseSession();
                    }
                }
                else {
                    moveToNextSet();
                }
                break;
            case R.id.fab_add:
                addSet();

        }
        refreshFab();
    }

    private void moveToNextSet() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        Set completedSet = mExerciseSession.getCurrentSet();
        int completedSetIndex = mExerciseSession.getCurrentSetIndex();
        mExerciseSession.completeCurrentSetAndMoveToNext();
        mSetsFragment.refreshNextSet(mCurrentSetIndex);
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        db.insertSet(mExerciseSession, completedSetIndex, completedSet);
    }

    private void addSet() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        mExerciseSession.generateNewSet();
        mSetsFragment.refreshSetAdded();
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        int lastSetPosition = mExerciseSession.getNumSets()-1;
        Set lastSet = mExerciseSession.getSet(lastSetPosition);
        db.insertSet(mExerciseSession, lastSetPosition, lastSet);
    }

    private void refreshFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        if (mExerciseSession.isCompleted()) {
            fab.setImageResource(R.drawable.ic_done_all_white_36dp);
            fabAdd.setVisibility(View.VISIBLE);
        }
        else {
            fab.setImageResource(R.drawable.ic_done_white_36dp);
            fabAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(edu.umn.paull011.evolveworkoutlogger.R.menu.menu_active_exercise_session, menu);
        MenuItem previous = menu.findItem(edu.umn.paull011.evolveworkoutlogger.R.id.previous_exercise);
        MenuItem next = menu.findItem(edu.umn.paull011.evolveworkoutlogger.R.id.next_exercise);
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
            case edu.umn.paull011.evolveworkoutlogger.R.id.previous_exercise:
                moveToPreviousExerciseSession();
                return true;
            case edu.umn.paull011.evolveworkoutlogger.R.id.next_exercise:
                moveToNextExerciseSession();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveToPreviousExerciseSession() {
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition - 1);
        startActivity(i);
        finish();
    }

    private void moveToNextExerciseSession() {
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition + 1);
        startActivity(i);
        finish();
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
                fragmentManager.findFragmentById(edu.umn.paull011.evolveworkoutlogger.R.id.fragment_exercise_session_sets);
        mSetsFragment.setExerciseSession(mExerciseSession);
    }
}