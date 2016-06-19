package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.fragments.SavedExercisesFragment;

public class MyExercises extends AppCompatActivity
        implements SavedExercisesFragment.OnFragmentInteractionListener {

    private SavedExercisesFragment mFragment;
    private static final String TAG = MyExercises.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_my_exercises);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("My Exercises");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragment = (SavedExercisesFragment)
                getSupportFragmentManager().findFragmentById(edu.umn.paull011.evolveworkoutlogger.R.id.fragment_saved_exercises);
    }

    @Override
    public void exerciseSelected(String exerciseName) {
        // View Exercise
        Intent i = new Intent(this, ViewExercise.class);
        i.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, exerciseName);
        startActivity(i);
    }

    public void handleButtonClick(View view) {
        int id = view.getId();
        Intent i;
        switch (id) {
            case edu.umn.paull011.evolveworkoutlogger.R.id.fab:
                i = new Intent(this, CreateExercise.class);
                startActivityForResult(i, ResponseCodes.NEW_EXERCISE.getValue());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                mFragment.refresh();
            }
        }
    }
}
