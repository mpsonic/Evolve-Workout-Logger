package edu.umn.paull011.evolveworkoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;

public class StartPage extends AppCompatActivity {

    private static final String TAG = StartPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.umn.paull011.evolveworkoutlogger.R.layout.activity_start_page);
        Toolbar toolbar = (Toolbar) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHelper db = DatabaseHelper.getInstance(getBaseContext());

        Runnable cleanDatabases = new Runnable() {
            @Override
            public void run() {
                DatabaseHelper db = DatabaseHelper.getInstance(getBaseContext());
                db.cleanTemporaryData();
            }
        };
        cleanDatabases.run();

        int daysSinceLastRoutineSession = db.getDaysSinceLastRoutineSession();
        TextView welcomeText = (TextView) findViewById(R.id.welcome_message);
        String welcomeMessage;
        if (daysSinceLastRoutineSession == -1) {
            welcomeMessage = "Welcome to Evolve Workout Logger!" +
                    " Start your first routine by pressing the button below";
        }
        else if (daysSinceLastRoutineSession == 0) {
            welcomeMessage = "Congrats! You've done your workout for today. Time to chill ";
                    //new String(Character.toChars(0x1f60e)); (sunglasses face)
        }
        else {
            welcomeMessage = "It's been " + daysSinceLastRoutineSession + " days since your last workout";
            if (daysSinceLastRoutineSession > 3) {
                welcomeMessage += " :'(";
            }
        }
        welcomeText.setText(welcomeMessage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(edu.umn.paull011.evolveworkoutlogger.R.menu.menu_start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        switch (id) {
//            case R.id.action_settings:
//                // Start settings activity
//                break;
            case edu.umn.paull011.evolveworkoutlogger.R.id.action_my_exercises:
                // Start my exercises activity
                i = new Intent(this, MyExercises.class);
                startActivity(i);
                break;
            case edu.umn.paull011.evolveworkoutlogger.R.id.action_my_routines:
                // Start my routines activity
                i = new Intent(this, MyRoutines.class);
                startActivity(i);
                break;
//            case R.id.action_stats:
//                // Start stats activity
//                break;
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
            case edu.umn.paull011.evolveworkoutlogger.R.id.button_start_routine:
                // Switch to the start routine activity
                intent = new Intent(this, StartRoutine.class);
                startActivity(intent);
                break;
            /*case edu.umn.paull011.evolveworkoutlogger.R.id.button_test_components:
                intent = new Intent(this, ComponentTesting.class);
                startActivity(intent);
                break;
            case edu.umn.paull011.evolveworkoutlogger.R.id.button_clear_data:
                DatabaseHelper db = DatabaseHelper.getInstance(this);
                db.makeFresh(this);*/
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
