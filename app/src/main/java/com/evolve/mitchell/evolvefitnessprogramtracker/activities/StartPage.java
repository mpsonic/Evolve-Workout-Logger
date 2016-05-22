package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;

public class StartPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        Runnable cleanDatabases = new Runnable() {
            @Override
            public void run() {
                DatabaseHelper db = new DatabaseHelper(getBaseContext());
                db.cleanDatabases();
                db.close();
            }
        };
        cleanDatabases.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                // Start settings activity
                break;
            case R.id.action_my_exercises:
                // Start my exercises activity
                break;
            case R.id.action_my_routines:
                // Start my routines activity
                break;
            case R.id.action_stats:
                // Start stats activity
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void handleButtonClick(View view) {
        Button button = (Button) view;
        Intent intent;
        switch (button.getId()) {
            case R.id.button_start_routine:
                // Switch to the start routine activity
                intent = new Intent(this, StartRoutine.class);
                startActivity(intent);
                break;
            case R.id.button_test_components:
                intent = new Intent(this, ComponentTesting.class);
                startActivity(intent);
                break;
        }
    }
}
