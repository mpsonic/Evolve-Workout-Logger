package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;


public class StartPage extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        /*// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Start Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                // Uri.parse(null),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                // android-app://<package_name>/<scheme>/[host_path]
                //Uri.parse("android-app://")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "StartPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.evolve.mitchell.evolvefitnessprogramtracker.activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }

    public void handleButtonClick(View view) {
        Button button = (Button) view;
        switch (button.getId()) {
            case R.id.button_start_routine:
                // Switch to the start routine activity
                Intent intent = new Intent(this, StartRoutine.class);
                startActivity(intent);
                break;
        }
    }
}
