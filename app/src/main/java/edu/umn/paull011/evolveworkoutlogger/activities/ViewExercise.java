package edu.umn.paull011.evolveworkoutlogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;
import edu.umn.paull011.evolveworkoutlogger.fragments.ExerciseStatsFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseStatsDataHolder;

public class ViewExercise extends AppCompatActivity implements ExerciseStatsFragment.OnFragmentInteractionListener{

    private ExerciseStatsDataHolder mDataHolder = ExerciseStatsDataHolder.getInstance();
    private ExerciseStatsFragment mFragment;

    private static final String TAG = ViewExercise.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        String exerciseName = getIntent().getStringExtra(DatabaseHelper.KEY_EXERCISE_NAME);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        Exercise exercise = db.getExercise(exerciseName);
        ExerciseStats exerciseStats = db.getExerciseStats(exercise);

        mDataHolder.setExercise(exercise);
        mDataHolder.setExerciseStats(exerciseStats);

        TextView timesPerformed = (TextView) findViewById(R.id.number_times_performed);
        TextView lastDatePerformed = (TextView) findViewById(R.id.date_last_performed);
        assert timesPerformed != null;
        assert lastDatePerformed != null;
        timesPerformed.setText(String.valueOf(exerciseStats.getTimesPerformed()));
        DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date date = exerciseStats.getDateLastPerformed();
        assert date != null;
        if (date.getTime() != 0) {
            lastDatePerformed.setText(format.format(date));
        }
        else {
            lastDatePerformed.setText(R.string.text_never);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(exerciseName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragment = (ExerciseStatsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_exercise_stats);
    }
}
