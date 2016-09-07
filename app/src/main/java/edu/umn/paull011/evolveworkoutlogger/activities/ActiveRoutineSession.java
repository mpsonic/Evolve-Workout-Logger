package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineSession;
import edu.umn.paull011.evolveworkoutlogger.fragments.RoutineSessionExercisesFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.AreYouSureDialog;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineSessionDataHolder;

public class ActiveRoutineSession extends AppCompatActivity
        implements RoutineSessionExercisesFragment.OnFragmentInteractionListener,
        AreYouSureDialog.DialogChooserListener{

    private Routine mRoutine;
    private RoutineSession mRoutineSession;
    private static final String TAG = ActiveRoutineSession.class.getSimpleName();
    private RoutineSessionDataHolder dataHolder = RoutineSessionDataHolder.getInstance();
    private RoutineSessionExercisesFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Store the routine id so that it can be retrieved from the database
        Intent intent = getIntent();
        String routineName = intent.getStringExtra(DatabaseHelper.KEY_ROUTINE_NAME);
        Boolean makeNewSession = intent.getBooleanExtra(StartRoutine.KEY_NEW_SESSION, true);

        // Get the routine and create a new routine session
        if (savedInstanceState == null) {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            mRoutine = db.getRoutine(routineName);
            if (makeNewSession) {
                mRoutineSession = mRoutine.createNewRoutineSession();
                db.insertRoutineSessionDeep(mRoutineSession);
            }
            else {
                mRoutineSession = db.getLastRoutineSession(mRoutine);
            }
            dataHolder.setRoutine(mRoutine);
            dataHolder.setRoutineSession(mRoutineSession);

        }
        else {
            mRoutine = dataHolder.getRoutine();
            mRoutineSession = dataHolder.getRoutineSession();
        }


        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_active_routine_session);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mRoutine.getName());
        setSupportActionBar(toolbar);

        /*TextView dateText = (TextView) findViewById(R.id.active_routine_session_date);
        assert dateText != null;
        Date date = mRoutineSession.getDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        dateText.setText(dateFormat.format(date));*/

        Button addExerciseButton = (Button) findViewById(R.id.button_add_exercise_to_routine_session);
        assert addExerciseButton != null;
        addExerciseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewExercise();
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AreYouSureDialog dialog = AreYouSureDialog.newInstance(
                                "Are you sure you want to finish your workout?"
                        );
                        dialog.show(getFragmentManager(), "AreYouSureDialog");
                    }
                }
        );

        EditText notesEdit = (EditText) findViewById(R.id.edit_notes);
        notesEdit.setText(mRoutineSession.getNotes());
        notesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRoutineSession.setNotes(String.valueOf(charSequence));
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("Reload", true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFragment.refresh();
    }

    // Called by RoutineSessionExercisesFragment in its onAttach method
    @Override
    public void setFragmentRoutineSession() {
        Log.d(TAG, "setFragmentRoutineSession");
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = (RoutineSessionExercisesFragment)
                fragmentManager.findFragmentById(edu.umn.paull011.evolveworkoutlogger.R.id.fragment_routine_session_exercises);
        mFragment.setRoutineSession(mRoutineSession);
    }

    // Called when the user touches one of the exercise sessions in the fragment RecyclerView
    @Override
    public void exerciseSessionSelected(int position) {
        Log.d(TAG, "exerciseSessionSelected");
        Intent i = new Intent(this, ActiveExerciseSession.class);
        i.putExtra(DatabaseHelper.KEY_POSITION, position);
        startActivity(i);
    }

    public void addNewExercise() {
        Log.d(TAG, "addNewExercise()");
        Intent addExercise = new Intent(this, AddExercise.class);
        startActivityForResult(addExercise, ResponseCodes.NEW_EXERCISE.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_EXERCISE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                // Add selected exercise to routine
                String exerciseName = data.getStringExtra(DatabaseHelper.KEY_EXERCISE_NAME);
                DatabaseHelper db = DatabaseHelper.getInstance(this);
                Exercise exercise = db.getExercise(exerciseName);
                mRoutineSession.addNewExerciseSessionFromExercise(exercise, true);
                db.insertRoutineExercise(
                        mRoutine.getName(),
                        exercise.getName(),
                        mRoutine.getNumExercises() - 1
                );
                mFragment.refresh();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        mRoutineSession.finish();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.insertRoutineSessionDeep(mRoutineSession);
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
