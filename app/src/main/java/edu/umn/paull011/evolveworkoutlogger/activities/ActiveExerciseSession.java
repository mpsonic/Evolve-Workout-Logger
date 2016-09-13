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
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseSessionDataHolder;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineSessionDataHolder;

public class ActiveExerciseSession extends AppCompatActivity implements
        ExerciseSessionSetsFragment.OnFragmentInteractionListener,
        ExerciseSession.OnExerciseSessionUpdateListener{

    private RoutineSessionDataHolder routineSessionDataHolder = RoutineSessionDataHolder.getInstance();
    private ExerciseSessionDataHolder exerciseSessionDataHolder = ExerciseSessionDataHolder.getInstance();
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
        mExercise = routineSessionDataHolder.getRoutine().getExercise(mExercisePosition);
        mRoutineSession = routineSessionDataHolder.getRoutineSession();
        mExerciseSession = mRoutineSession.getExerciseSession(mExercisePosition);
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        exerciseSessionDataHolder.setExercise(mExercise);
        exerciseSessionDataHolder.setExerciseSession(mExerciseSession);

        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_active_exercise_session);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mExercise.getName() + "  (" +
                String.valueOf(mExercisePosition + 1) + "/" + mRoutineSession.getExerciseSessionCount() + ")");
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

        refreshFab();
    }

    @Override
    public void onExerciseSessionUpdate() {
        Log.d(TAG,"onExerciseSessionUpdate");
        refreshFab();
        mSetsFragment.hideOrShowEmptyView();
    }

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart");
        super.onStart();
        mExerciseSession.setOnExerciseSessionUpdatedListener(this);
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
        mExerciseSession.removeExerciseSessionUpdateListener();
    }

    private void handleButtonClick(View view) {
        Log.d(TAG,"handleButtonClick");
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                if (mExerciseSession.getNumSets() == 0) {
                    addSet();
                }
                else {
                    if (mExerciseSession.isCompleted()) {
                        if (mExercisePosition == mRoutineSession.getExerciseSessionCount() - 1) {
                            NavUtils.navigateUpFromSameTask(this);
                        }
                        else {
                            moveToNextExerciseSession();
                        }
                    }
                    else {
                        moveToNextSet();
                    }
                }
                break;
            case R.id.fab_add:
                addSet();
        }
        refreshFab();
    }

    private void moveToNextSet() {
        Log.d(TAG,"moveToNextSet");
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        Set completedSet = mExerciseSession.getCurrentSet();
        int completedSetIndex = mExerciseSession.getCurrentSetIndex();
        mExerciseSession.completeCurrentSetAndMoveToNext();
        mSetsFragment.refreshNextSet(completedSetIndex);
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        db.insertSet(mExerciseSession, completedSetIndex, completedSet);
    }

    private void addSet() {
        Log.d(TAG,"addSet");
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        mExerciseSession.generateNewSet();
        mSetsFragment.refreshSetAdded();
        mCurrentSetIndex = mExerciseSession.getCurrentSetIndex();
        int lastSetPosition = mExerciseSession.getNumSets()-1;
        Set lastSet = mExerciseSession.getSet(lastSetPosition);
        db.insertSet(mExerciseSession, lastSetPosition, lastSet);
    }

    private void refreshFab() {
        Log.d(TAG,"refreshFab");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        float density = getResources().getDisplayMetrics().density;
        if (mExerciseSession.getNumSets() == 0) {
            /*CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            params.setAnchorId(View.NO_ID);
            params.bottomMargin = ((int) density * 16);
            fabAdd.setLayoutParams(params);*/
            fab.setImageResource(R.drawable.ic_add_white_36dp);
            fabAdd.hide();
        }
        else {
            if (mExerciseSession.isCompleted()) {
                fab.setImageResource(R.drawable.ic_done_all_white_36dp);
                fab.show();
                /*CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                params.bottomMargin = ((int) density * 96);
                fabAdd.setLayoutParams(params);*/
                fabAdd.show();
            }
            else {
                fab.setImageResource(R.drawable.ic_done_white_36dp);
                fab.show();
                fabAdd.hide();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(edu.umn.paull011.evolveworkoutlogger.R.menu.menu_active_exercise_session, menu);
        MenuItem previous = menu.findItem(edu.umn.paull011.evolveworkoutlogger.R.id.previous_exercise);
        MenuItem next = menu.findItem(edu.umn.paull011.evolveworkoutlogger.R.id.next_exercise);
        int numExercises = routineSessionDataHolder.getRoutineSession().getExerciseSessionCount();
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
        Log.d(TAG,"onOptionsItemSelected");
        int id = item.getItemId();
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
        Log.d(TAG,"moveToPreviousExerciseSession");
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition - 1);
        startActivity(i);
        finish();
    }

    private void moveToNextExerciseSession() {
        Log.d(TAG,"moveToNextExerciseSession");
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, mExercisePosition + 1);
        startActivity(i);
        finish();
        /*overridePendingTransition(R.anim.slide_in_up, R.anim.nothing);*/
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

    // Removes focus from EditTexts when any other area of the activity is touched
    // Doesn't allow ButtonEditText buttons to work
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/
}