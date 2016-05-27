package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.SavedRoutinesFragment;

public class StartRoutine extends AppCompatActivity implements
        SavedRoutinesFragment.OnFragmentInteractionListener {

    private SavedRoutinesFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragment = (SavedRoutinesFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_saved_routines);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_start_routine);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateRoutineActivity();
            }
        });
    }

    public void routineSelected(long id) {
        /*String message = "Selected Routine " + id;
        Toast toast = new Toast(this);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();*/

        Intent i = new Intent(this, ActiveRoutineSession.class);
        i.putExtra(DatabaseHelper.KEY_ROUTINE_NAME, id);
        i.putExtra(ActivityEnum.SENDER, ActivityEnum.START_ROUTINE);
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
}
