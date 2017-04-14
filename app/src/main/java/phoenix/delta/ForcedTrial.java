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

public class ForcedTrial extends ActionBarActivity {
    Button now_btn, wait_btn, pause_btn, resume_btn;
    Procedure currProcedure;
    private long startTime, timeWhenPaused, responseTime;
    ScheduleChoice forcedChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_forced);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        // create a timer to time the response time of the participant
        startTime = SystemClock.elapsedRealtime();
        timeWhenPaused = 0;
        responseTime = 0;

        now_btn = (Button)findViewById(R.id.btn_now);
        wait_btn = (Button)findViewById(R.id.btn_wait);

        int completedTrials;
        int blockNumber;
        if (currSession.currentBlock == null)
        {
            completedTrials = 0;
            blockNumber = 0;
        }
        else {
            completedTrials = currSession.currentBlock.trials.size();
            blockNumber = currSession.currentBlock.blockNumber;
        }
        boolean blockMod = blockNumber % 2 == 0;
        boolean trialMod = completedTrials == 0;
        /*
        | blockMod | trialMod | result
        |----------|----------|-------
        |    0     |    0     | Now
        |    0     |    1     | Wait
        |    1     |    0     | Wait
        |    1     |    1     | Now
        */
        if (blockMod ^ trialMod) {
            // now
            now_btn.setVisibility(View.VISIBLE);
            wait_btn.setVisibility(View.GONE);
            forcedChoice = ScheduleChoice.INSTANT_GAME_ACCESS;
        } else {
            // wait
            now_btn.setVisibility(View.GONE);
            wait_btn.setVisibility(View.VISIBLE);
            forcedChoice = ScheduleChoice.WAIT_FOR_GAME;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // record response time
                timeWhenPaused = SystemClock.elapsedRealtime() - startTime;
                responseTime += timeWhenPaused;

                // save the data
                currSession.setStudentResponseTime(responseTime / 1000.0);
                currSession.setStudentSelection(forcedChoice);
                //currSession.endTrial();

                // go to next activity
                Intent playNowAct = new Intent(ForcedTrial.this, TrialWaitActivity.class);
                playNowAct.putExtra("PROCEDURE", currProcedure);
                startActivity(playNowAct);
            }
        };

        now_btn.setOnClickListener(listener);
        wait_btn.setOnClickListener(listener);

        pause_btn = (Button)findViewById(R.id.btn_pause);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeWhenPaused = SystemClock.elapsedRealtime() - startTime;
                responseTime += timeWhenPaused;

                now_btn.setClickable(false);
                pause_btn.setVisibility(View.INVISIBLE);
                resume_btn.setVisibility(View.VISIBLE);

                Toast.makeText(ForcedTrial.this, "Paused", Toast.LENGTH_SHORT).show();
            }
        });

        resume_btn = (Button)findViewById(R.id.btn_resume);
        resume_btn.setVisibility(View.INVISIBLE);
        resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTime = SystemClock.elapsedRealtime();

                now_btn.setClickable(true);
                pause_btn.setVisibility(View.VISIBLE);
                resume_btn.setVisibility(View.INVISIBLE);

                Toast.makeText(ForcedTrial.this, "Resumed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed () {
        // do nothing
    }

}
