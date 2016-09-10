package edu.umn.paull011.evolveworkoutlogger.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.fragments.SavedRoutinesFragment;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.AreYouSureDialog;

public class MyRoutines extends AppCompatActivity
        implements SavedRoutinesFragment.OnFragmentInteractionListener,
        AreYouSureDialog.DialogChooserListener {

    SavedRoutinesFragment mFragment;
    private static final String TAG = MyRoutines.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("My Routines");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragment = (SavedRoutinesFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_saved_routines);
    }

    @Override
    public void routineSelected(String routineName) {
        Intent i = new Intent(this, ViewRoutine.class);
        i.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, routineName);
        startActivity(i);
    }

    @Override
    public boolean routinesDeletable() {
        return true;
    }

    public void handleButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                Intent i = new Intent(this, CreateRoutine.class);
                startActivityForResult(i, ResponseCodes.NEW_ROUTINE.getValue());
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mFragment.deleteSwipedRoutineFromAdapter();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mFragment.unDismissSwipedRoutineFromAdapter();
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
}
