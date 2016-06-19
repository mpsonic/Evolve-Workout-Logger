package edu.umn.paull011.evolveworkoutlogger.activities;

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

        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_active_exercise_session);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mExercise.getName() + "  (" +
                String.valueOf(mExercisePosition + 1) + "/" + routineSession.getExerciseCount() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addSetButton = (Button) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.button_add_set);
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
        );
    }

    private void handleButtonClick(View view) {
        int id = view.getId();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        switch (id) {
            case R.id.button_add_set:
                mExerciseSession.generateNewSet();
                mSetsFragment.refreshSetAdded();
                mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
                int lastSetPosition = mExerciseSession.getNumSets()-1;
                Set lastSet = mExerciseSession.getSet(lastSetPosition);
                db.insertSet(mExerciseSession, lastSetPosition, lastSet);
                break;
            case R.id.button_next_set:
                Set completedSet = mExerciseSession.getCurrentSet();
                int completedSetIndex = mExerciseSession.getCurrentSetIndex();
                mExerciseSession.completeCurrentSetAndMoveToNext();
                mSetsFragment.refreshNextSet(mCurrentSetIndex);
                mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
                db.insertSet(mExerciseSession, completedSetIndex, completedSet);
                break;
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
                i = new Intent(this, ActiveExerciseSession.class);
                i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition - 1);
                startActivity(i);
                finish();
                return true;
            case edu.umn.paull011.evolveworkoutlogger.R.id.next_exercise:
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
                fragmentManager.findFragmentById(edu.umn.paull011.evolveworkoutlogger.R.id.fragment_exercise_session_sets);
        mSetsFragment.setExerciseSession(mExerciseSession);
    }
}