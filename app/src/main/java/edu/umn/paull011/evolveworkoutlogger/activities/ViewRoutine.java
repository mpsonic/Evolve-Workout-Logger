package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineStats;
import edu.umn.paull011.evolveworkoutlogger.fragments.RoutineExercisesFragment;
import edu.umn.paull011.evolveworkoutlogger.fragments.RoutineHistoryFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineStatsDataHolder;

public class ViewRoutine extends AppCompatActivity
        implements RoutineExercisesFragment.OnFragmentInteractionListener,
        RoutineHistoryFragment.OnFragmentInteractionListener{

    private Routine mRoutine;
    private RoutineStats mRoutineStats;
    private RoutineStatsDataHolder mDataHolder = RoutineStatsDataHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        String routineName = getIntent().getStringExtra(DatabaseHelper.KEY_ROUTINE_NAME);
        mRoutine = db.getRoutine(routineName);
        mRoutineStats = db.getRoutineStats(mRoutine);
        mDataHolder.setRoutine(mRoutine);
        mDataHolder.setRoutineStats(mRoutineStats);

        setContentView(R.layout.activity_view_routine);

        TextView lastPerformedDate = (TextView) findViewById(R.id.date_last_performed);
        TextView numTimesPerformed = (TextView) findViewById(R.id.number_times_performed);

        String lpdString = mRoutineStats.getLastPerformedDateString();
        if (lpdString != null) {
            lastPerformedDate.setText(lpdString);
        }
        else {
            lastPerformedDate.setText(R.string.text_never);
        }

        int timesPerformed = mRoutineStats.getNumDates();
        numTimesPerformed.setText(String.valueOf(timesPerformed));

        FragmentManager fm = this.getSupportFragmentManager();
        RoutineExercisesFragment fragment = (RoutineExercisesFragment) fm.findFragmentById(R.id.fragment_view_routine_exercises);
        fragment.setRoutine(mRoutine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(routineName);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void exerciseSelected(String exerciseName) {
        // View Exercise
        Intent i = new Intent(this, ViewExercise.class);
        i.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, exerciseName);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.action_edit:
                // Start Edit routine activity
                i = new Intent(this, CreateRoutine.class);
                i.putExtra("Edit", true);
                i.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, mRoutine.getName());
                startActivityForResult(i, ResponseCodes.EDIT_ROUTINE.getValue());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResponseCodes.EDIT_ROUTINE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                setIntent(data);
                recreate();
            }
        }
    }
}
