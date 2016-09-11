package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;
import edu.umn.paull011.evolveworkoutlogger.fragments.ExerciseHistoryFragment;
import edu.umn.paull011.evolveworkoutlogger.fragments.ExerciseStatsFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseStatsDataHolder;

public class ViewExercise extends AppCompatActivity
        implements ExerciseStatsFragment.OnFragmentInteractionListener,
        ExerciseHistoryFragment.OnFragmentInteractionListener {

    private static final String TAG = ViewExercise.class.getSimpleName();
    private ExerciseStatsDataHolder mDataHolder = ExerciseStatsDataHolder.getInstance();
    private ExerciseStatsFragment mStatsFragment;
    private ExerciseHistoryFragment mHistoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        String exerciseName = getIntent().getStringExtra(DatabaseHelper.KEY_EXERCISE_NAME);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        Exercise exercise = db.getExercise(exerciseName);
        ExerciseStats exerciseStats = db.getExerciseStats(exercise);

        mDataHolder.setExercise(exercise);
        mDataHolder.setExerciseStats(exerciseStats);

        setContentView(R.layout.activity_view_exercise);

        TextView timesPerformed = (TextView) findViewById(R.id.number_times_performed);
        TextView lastDatePerformed = (TextView) findViewById(R.id.date_last_performed);
        assert timesPerformed != null;
        assert lastDatePerformed != null;
        timesPerformed.setText(String.valueOf(exerciseStats.getTimesPerformed()));
        DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String dateString = exerciseStats.getLastPerformedDateString();
        if (dateString != null) {
            lastDatePerformed.setText(dateString);
        }
        else {
            lastDatePerformed.setText(R.string.text_never);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(exerciseName);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatsFragment = (ExerciseStatsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_exercise_stats);
        mHistoryFragment = (ExerciseHistoryFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_exercise_history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_exercise, menu);
        MenuItem edit = menu.findItem(R.id.edit_exercise);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_exercise:
                launchEditExerciseActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchEditExerciseActivity() {
        Intent i = new Intent (this, CreateExercise.class);
        i.putExtra("Edit", true);
        i.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, mDataHolder.getExercise().getName());
        startActivityForResult(i, ResponseCodes.EDIT_EXERCISE.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.EDIT_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                this.recreate();
            }
        }
    }
}
