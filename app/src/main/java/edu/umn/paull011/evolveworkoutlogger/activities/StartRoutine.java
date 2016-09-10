package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Calendar;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.fragments.SavedRoutinesFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.AreYouSureDialog;

public class StartRoutine extends AppCompatActivity implements
        SavedRoutinesFragment.OnFragmentInteractionListener,
        AreYouSureDialog.DialogChooserListener{

    private SavedRoutinesFragment mFragment;
    private String mSelectedRoutine;
    public static final String KEY_NEW_SESSION = "FLAG_NEW";
    private static final String TAG = StartRoutine.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragment = (SavedRoutinesFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_saved_routines);
        FloatingActionButton fab = (FloatingActionButton) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.fab_start_routine);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateRoutineActivity();
            }
        });
    }

    public void routineSelected(String routineName) {
        /*String message = "Selected Routine " + id;
        Toast toast = new Toast(this);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();*/
        mSelectedRoutine = routineName;
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        // This is commented out in order to test exercise measurement incrementing
        // TODO: uncomment once increment bugs are fixed
        /*if (db.routineSessionExistsForDate(routineName, today)) {
            AreYouSureDialog dialog = AreYouSureDialog.newInstance(
                    "A routine session for \"" + routineName + "\" has already been started for today. Continue routine?"
            );
            dialog.show(getFragmentManager(), "AreYouSureDialog");
        }
        else {
            launchNewRoutineSession(routineName);
        }*/
        launchNewRoutineSession(routineName);
    }

    @Override
    public boolean routinesDeletable() {
        return false;
    }

    public void launchExistingRoutineSession(String routineName) {
        Intent i = new Intent(this, ActiveRoutineSession.class);
        i.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, routineName);
        i.putExtra(KEY_NEW_SESSION, false);
        startActivity(i);
        finish();
    }

    public void launchNewRoutineSession(String routineName) {
        Intent i = new Intent(this, ActiveRoutineSession.class);
        i.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, routineName);
        i.putExtra(KEY_NEW_SESSION, true);
        startActivity(i);
        finish();
    }

    private void launchCreateRoutineActivity(){
        Intent i = new Intent(this, CreateRoutine.class);
        startActivityForResult(i, ResponseCodes.NEW_ROUTINE.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ResponseCodes.NEW_ROUTINE.getValue()) {
            if (resultCode == Activity.RESULT_OK) {
                mFragment.refresh();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        launchExistingRoutineSession(mSelectedRoutine);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
