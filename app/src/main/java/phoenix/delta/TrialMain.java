package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class TrialMain extends ActionBarActivity {

    Button start_trial_btn;
    Procedure currProcedure;
    MediaPlayer alertSnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_main);
        alertSnd = MediaPlayer.create(this, R.raw.save);
        alertSnd.start();


        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;

        if(!currSession.isTrialNull())
            currSession.endTrial();

        if(currSession.isNewSession())
            currSession.setTimerInvisible();

        // check if sessionType is finished
        if(currSession.isSessionDone()) {


            // go back to the admin/ teacher page
            Toast.makeText(TrialMain.this, "Done with Session", Toast.LENGTH_SHORT).show();

            // go to done sessionType activity
            Intent doneSesAct = new Intent(TrialMain.this,DoneSessionActivity.class);
            doneSesAct.putExtra("PROCEDURE", currProcedure);
            startActivity(doneSesAct);


        }

        start_trial_btn = (Button)findViewById(R.id.btn_start_trial);
        start_trial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trialSelection;
                if(startTrial()) {
                    int completedTrials = currSession.currentBlock.trials.size();
                    if (completedTrials > 1) {
                        trialSelection = new Intent(TrialMain.this,TrialSelection.class);
                    } else {
                            trialSelection = new Intent(TrialMain.this,ForcedTrial.class);
                    }
                    trialSelection.putExtra("PROCEDURE", currProcedure);
                    Toast.makeText(TrialMain.this, "START TRIAL!", Toast.LENGTH_SHORT).show();
                    startActivity(trialSelection);
                }
                else
                    Toast.makeText(TrialMain.this, "CANNOT START NEW TRIAL", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // set up new trial
    public boolean startTrial () {
        return currProcedure.currentSession.startNewTrial();
    }

    @Override
    public void onBackPressed () {
        // do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent goBack;
        final Session currSession = currProcedure.currentSession;
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.quitSave_trial :
                goBack = new Intent(TrialMain.this,SessionPrep.class);

                // save data & reset
                currProcedure.endSession(getApplicationContext());

                goBack.putExtra("PROCEDURE", currProcedure);
                Toast.makeText(TrialMain.this, "Data is Saved!", Toast.LENGTH_SHORT).show();
                startActivity(goBack);
                break;
            case R.id.quitNoSave_trial :
                goBack = new Intent(TrialMain.this,SessionPrep.class);

                // reset
                currSession.resetSession();

                goBack.putExtra("PROCEDURE", currProcedure);
                Toast.makeText(TrialMain.this, "Data is not Saved!", Toast.LENGTH_SHORT).show();
                startActivity(goBack);
                break;
            case R.id.hide_timer :
                currSession.setTimerInvisible();
                Toast.makeText(TrialMain.this, "Timer is INVISIBLE during wait time", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_timer :
                currSession.setTimerVisible();
                Toast.makeText(TrialMain.this, "Timer is VISIBLE during wait time", Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
