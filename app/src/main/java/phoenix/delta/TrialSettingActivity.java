package phoenix.delta;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class TrialSettingActivity extends ActionBarActivity {

    Session currSession;
    //String[] nt, itp, tip, iinp;
    //NumberPicker num_trial_picker, init_time_picker, time_inc_picker, inc_interval_num_picker;
    Button save_change_btn, default_btn, cancel_btn;
    EditText total_num_trial, init_trial_time, game_time_delay, game_time_instant, wait_time_inc;
    Intent resultIntent;
    boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_setting);

        //////////// get session from admin activity
        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");
        //Toast.makeText(TrialSettingActivity.this, currSession.toString(), Toast.LENGTH_SHORT).show();

        total_num_trial = (EditText) findViewById(R.id.num_trial);
        init_trial_time = (EditText) findViewById(R.id.trial_time);
        game_time_delay = (EditText) findViewById(R.id.delay_game_time);
        game_time_instant = (EditText) findViewById(R.id.instant_game_time);
        wait_time_inc = (EditText) findViewById(R.id.wait_time_inc);

        // set to display current values
        total_num_trial.setText(Integer.toString(currSession.getNumTrial()));
//        init_trial_time.setText(Integer.toString(currSession.getInitTrialDurationTime()));
//        game_time_delay.setText(Integer.toString(currSession.getGameTimeDelay()));
//        game_time_instant.setText(Integer.toString(currSession.getGameTimeInstant()));
//        wait_time_inc.setText(Integer.toString(currSession.getTimeIncAmount()));

        total_num_trial.setGravity(Gravity.CENTER);
        init_trial_time.setGravity(Gravity.CENTER);
        game_time_delay.setGravity(Gravity.CENTER);
        game_time_instant.setGravity(Gravity.CENTER);
        wait_time_inc.setGravity(Gravity.CENTER);

        //////////// save change button
        save_change_btn = (Button)findViewById(R.id.save_change);
        save_change_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int numTrial = Integer.parseInt(total_num_trial.getText().toString());
                int trialTime = Integer.parseInt(init_trial_time.getText().toString());
                int gameTimeDelay = Integer.parseInt(game_time_delay.getText().toString());
                int gameTimeInstant = Integer.parseInt(game_time_instant.getText().toString());
                int waitTimeInc = Integer.parseInt(wait_time_inc.getText().toString());

                if (    numTrial > 0 && trialTime > 0 && gameTimeDelay > 0 && gameTimeInstant > 0 &&
                        gameTimeDelay < trialTime && gameTimeInstant < trialTime &&
                        waitTimeInc >= 0 &&
                        gameTimeDelay > gameTimeInstant) {

                    // save settingw3
                    currSession.changeSetting(numTrial, trialTime, gameTimeDelay, gameTimeInstant, waitTimeInc);
                    displayToast("Changes Saved!");
                    Intent resultIntent = new Intent(TrialSettingActivity.this, AdminActivity.class);
                    resultIntent.putExtra("SESSION", currSession);
                    startActivity(resultIntent);
                }
                else { // print useful error message

                    String err = "Invalid Input: ";
                    if(numTrial <= 0 || trialTime <= 0 || gameTimeDelay <= 0 || gameTimeInstant <= 0)
                        err += "Values must be positive!";
                    else if (gameTimeDelay >= trialTime || gameTimeInstant >= trialTime)
                        err += "Game time must be less than trail time";
                    else if (waitTimeInc < 0)
                        err += "Wait time must not be negative";
                    else if (gameTimeDelay <= gameTimeInstant)
                        err += "Delay game time should be greater than Instant game time";

                    displayToast(err);
                }


            }
        });

        //////////// default setting button
        default_btn = (Button)findViewById(R.id.default_btn);
        default_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                currSession.setDefaultSetting();
                displayToast("Apply Default Setting");
                Intent resultIntent = new Intent(TrialSettingActivity.this, AdminActivity.class);
                resultIntent.putExtra("SESSION", currSession);
                startActivity(resultIntent);
            }
        });

        //////////// cancel button (don't change anything)
        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayToast("No Change");
                Intent resultIntent = new Intent(TrialSettingActivity.this, AdminActivity.class);
                resultIntent.putExtra("SESSION", currSession);
                startActivity(resultIntent);
            }
        });
    }

    void displayToast (String toast) {
        Toast.makeText(TrialSettingActivity.this, toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
