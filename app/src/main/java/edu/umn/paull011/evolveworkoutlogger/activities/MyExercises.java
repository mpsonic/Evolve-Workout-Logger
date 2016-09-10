package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.fragments.SavedExercisesFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.AreYouSureDialog;

public class MyExercises extends AppCompatActivity
        implements SavedExercisesFragment.OnFragmentInteractionListener,
        AreYouSureDialog.DialogChooserListener{

    private SavedExercisesFragment mFragment;
    private static final String TAG = MyExercises.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
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

    @Override
    public boolean exercisesDeletable() {
        return true;
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
    public void onDialogPositiveClick(DialogFragment dialog) {
        mFragment.deleteSwipedExerciseFromAdapter();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mFragment.unDismissSwipedExerciseFromAdapter();
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
