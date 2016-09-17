package edu.umn.paull011.evolveworkoutlogger.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;

/***
 * An activity directing users towards a survey
 */
public class FeedbackPage extends AppCompatActivity {

    private TextView mFeedbackText;
    private Button mSurveyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Give Feedback");
        setSupportActionBar(toolbar);

        mSurveyButton = (Button) findViewById(R.id.survey_button);
        mSurveyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToSurvey();
                        setSurveyTakenState();
                    }
                }
        );

        mFeedbackText = (TextView) findViewById(R.id.feedback_text);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void goToSurvey() {
        Uri uriUrl = Uri.parse(getResources().getString(R.string.survey_url));
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void setSurveyTakenState() {
        mSurveyButton.setVisibility(View.GONE);
        mFeedbackText.setText(getResources().getString(R.string.survey_taken_message));
    }

}
