package phoenix.delta;

import android.content.Intent;
import android.os.*;
import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class TrialSelection extends ActionBarActivity {

    Button wait_btn, now_btn, pause_btn, resume_btn;
    private Chronometer counter;
    private long startTime, timeWhenPaused;
    private int responseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_selection);

        // create a timer to time the response time of the participant
        counter = (Chronometer) findViewById(R.id.chronometer);
        startTime = SystemClock.elapsedRealtime();
        counter.setBase(startTime);
        timeWhenPaused = 0;
        responseTime = 0;
        counter.start();

        now_btn = (Button)findViewById(R.id.btn_now);
        now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter.stop();

                // record response time
                String timeText = counter.getText().toString();
                String array[] = timeText.split(":");
                if(array.length == 2) {
                    // array[0] = minute, array[1] = second
                    int min =  Integer.parseInt(array[0]);
                    int sec = Integer.parseInt(array[1]);
                    responseTime = min * 60 + sec;
                }
                else if (array.length == 3) {
                    // array[0] = hour, array[1] = minute, array[2] = second
                    int hour = Integer.parseInt(array[0]);
                    int min =  Integer.parseInt(array[1]);
                    int sec = Integer.parseInt(array[2]);
                    responseTime = hour * 3600 + min * 60 + sec;
                }

                Toast.makeText(TrialSelection.this, "*" + responseTime + " sec* Instant Game Access", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TrialSelection.this,DroidRunJumpActivity.class));
            }
        });

        wait_btn = (Button)findViewById(R.id.btn_wait);
        wait_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter.stop();

                // record response time
                String timeText = counter.getText().toString();
                String array[] = timeText.split(":");
                // array[0] = minute, array[1] = second
                if(array.length == 2) {
                    responseTime = Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1]);
                }

                Toast.makeText(TrialSelection.this, "*" + responseTime + " sec* Wait to Play for Longer", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TrialSelection.this,TrialWaitActivity.class));
            }
        });

        pause_btn = (Button)findViewById(R.id.btn_pause);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeWhenPaused = SystemClock.elapsedRealtime() - counter.getBase();
                counter.stop();

                now_btn.setClickable(false);
                wait_btn.setClickable(false);
                pause_btn.setVisibility(View.INVISIBLE);
                resume_btn.setVisibility(View.VISIBLE);

                Toast.makeText(TrialSelection.this, "Paused", Toast.LENGTH_SHORT).show();
            }
        });

        resume_btn = (Button)findViewById(R.id.btn_resume);
        resume_btn.setVisibility(View.INVISIBLE);
        resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter.setBase(SystemClock.elapsedRealtime() - timeWhenPaused);
                counter.start();

                now_btn.setClickable(true);
                wait_btn.setClickable(true);
                pause_btn.setVisibility(View.VISIBLE);
                resume_btn.setVisibility(View.INVISIBLE);

                Toast.makeText(TrialSelection.this, "Resumed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial_selection, menu);
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
