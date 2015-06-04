package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;



public class TrialMain extends ActionBarActivity {

    Button start_trial_btn, save_quit_btn, nosave_quit_btn;
    Session currSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_main);



        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        if(!currSession.isTrialNull())
            currSession.endTrial();

        if(currSession.isNewSession())
            currSession.setTimerInvisible();

        // check if session is finished
        if(currSession.isSessionDone()) {


            // go back to the admin/ teacher page
            Toast.makeText(TrialMain.this, "Done with Session", Toast.LENGTH_SHORT).show();

            // go to done session activity
            Intent doneSesAct = new Intent(TrialMain.this,DoneSessionActivity.class);
            doneSesAct.putExtra("SESSION", currSession);
            startActivity(doneSesAct);


        }

        start_trial_btn = (Button)findViewById(R.id.btn_start_trial);
        start_trial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startTrial()) {
                    Intent trialSelection = new Intent(TrialMain.this,TrialSelection.class);
                    trialSelection.putExtra("SESSION", currSession);
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
        //int numTrial = currSession.getNextTrialTime();
        //Trial newTrial = new Trial(numTrial);
        Trial newTrial = new Trial();
        return currSession.startNewTrial(newTrial);
    }

    @Override
    public void onBackPressed () {
        // do nothing
        ;
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
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.quitSave_trial :
                if(currSession.isStartedByAdmin())
                    goBack = new Intent(TrialMain.this,AdminActivity.class);
                else
                    goBack = new Intent(TrialMain.this,TeacherActivity.class);

                // save data & reset
                currSession.endSession(getApplicationContext());

                goBack.putExtra("SESSION", currSession);
                Toast.makeText(TrialMain.this, "Data is Saved!", Toast.LENGTH_SHORT).show();
                startActivity(goBack);
                break;
            case R.id.quitNoSave_trial :
                if(currSession.isStartedByAdmin())
                    goBack = new Intent(TrialMain.this,AdminActivity.class);
                else
                    goBack = new Intent(TrialMain.this,TeacherActivity.class);

                // reset
                currSession.resetSession();

                goBack.putExtra("SESSION", currSession);
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
