package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jessica on 8/18/2016.
 */
public class TrialWait extends ActionBarActivity {

    Button wait_btn, pause_btn, resume_btn;
    Session currSession;
    private long startTime, timeWhenPaused, responseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_forced_wait);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        // create a timer to time the response time of the participant
        startTime = SystemClock.elapsedRealtime();
        timeWhenPaused = 0;
        responseTime = 0;


        wait_btn = (Button)findViewById(R.id.btn_wait);
        wait_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // record response time
                timeWhenPaused = SystemClock.elapsedRealtime() - startTime;
                responseTime += timeWhenPaused;

                // save the data
                currSession.setStudentResponseTime(responseTime / 1000.00);
                currSession.setStudentSelection(ScheduleChoice.WAIT_FOR_GAME);
                //currSession.endTrial();

                // go to next activity
                Intent waitAct = new Intent(TrialWait.this,TrialWaitActivity.class);
                waitAct.putExtra("SESSION", currSession);
                Toast.makeText(TrialWait.this, "*" + responseTime / 1000.0 + " sec* Wait to Play for Longer", Toast.LENGTH_SHORT).show();
                startActivity(waitAct);
            }
        });

        pause_btn = (Button)findViewById(R.id.btn_pause);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeWhenPaused = SystemClock.elapsedRealtime() - startTime;
                responseTime += timeWhenPaused;

                wait_btn.setClickable(false);
                pause_btn.setVisibility(View.INVISIBLE);
                resume_btn.setVisibility(View.VISIBLE);

                Toast.makeText(TrialWait.this, "Paused", Toast.LENGTH_SHORT).show();
            }
        });

        resume_btn = (Button)findViewById(R.id.btn_resume);
        resume_btn.setVisibility(View.INVISIBLE);
        resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTime = SystemClock.elapsedRealtime();

                wait_btn.setClickable(true);
                pause_btn.setVisibility(View.VISIBLE);
                resume_btn.setVisibility(View.INVISIBLE);

                Toast.makeText(TrialWait.this, "Resumed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed () {
        // do nothing
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
