package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;

public class MyRoutines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // TODO: Implement routineSelected method so that the active_routine_session activity opens for the specified routine
    public void routineSelected(long id) {
        String message = "Selected Routine " + id;
        Toast toast = new Toast(this);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
