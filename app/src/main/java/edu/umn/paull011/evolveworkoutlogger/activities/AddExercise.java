package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.fragments.SavedExercisesFragment;

public class AddExercise extends AppCompatActivity
        implements SavedExercisesFragment.OnFragmentInteractionListener {

    private FloatingActionButton fab;
    private SavedExercisesFragment mFragment;
    private static final String TAG = AddExercise.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_add_exercise);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragment = (SavedExercisesFragment) getSupportFragmentManager().findFragmentById(edu.umn.paull011.evolveworkoutlogger.R.id.fragment_saved_exercises);

        fab = (FloatingActionButton) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExercise();
            }
        });
    }

    public void createExercise() {
        Intent createExercise = new Intent(this, CreateExercise.class);
        startActivityForResult(createExercise, ResponseCodes.NEW_EXERCISE.getValue());
    }

    @Override
    public void exerciseSelected(String exerciseName) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, exerciseName);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(resultCode, data);
                finish();
            }
        }
    }

    @Override
    public boolean exercisesDeletable() {
        return false;
    }
}
