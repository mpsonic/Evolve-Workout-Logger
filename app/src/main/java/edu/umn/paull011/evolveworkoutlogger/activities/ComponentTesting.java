package edu.umn.paull011.evolveworkoutlogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.umn.paull011.evolveworkoutlogger.R;

public class ComponentTesting extends AppCompatActivity {

    private static final String TAG = ComponentTesting.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_testing);
    }
}
