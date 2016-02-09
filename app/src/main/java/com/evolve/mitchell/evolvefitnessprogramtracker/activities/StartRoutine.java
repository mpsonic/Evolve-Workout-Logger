package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.fragments.SavedRoutinesFragment;

public class StartRoutine extends AppCompatActivity implements
        SavedRoutinesFragment.OnFragmentInteractionListener {

    public static final String ROUTINE_ID_NAME = "RoutineId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get floating action button
        // Set functionality inside of onClick method
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_start_routine);
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
        i.putExtra(ROUTINE_ID_NAME, id);
        startActivity(i);
    }

    private void launchCreateRoutineActivity(){
        Intent i = new Intent(this, CreateRoutine.class);
        startActivity(i);
    }
}
