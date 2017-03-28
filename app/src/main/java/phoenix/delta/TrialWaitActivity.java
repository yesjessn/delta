/*package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.widget.TextView;


public class TrialWaitActivity extends ActionBarActivity {

    TextView msg;
    Session currSession;
    GIFView gifView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_wait);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        gifView = (GIFView)findViewById(R.id.gifView);
        msg = (TextView) findViewById(R.id.countdown_msg);
        int waitTime = currSession.getWaitTime();

        new CountDownTimer (10000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(currSession.isTimerVisible())
                    msg.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent playNowAct = new Intent(TrialWaitActivity.this,DroidRunJumpActivity.class);
                playNowAct.putExtra("SESSION", currSession);
                startActivity(playNowAct);
                //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
            }

        }.start();
        //startActivity(new Intent(TrialWaitActivity.this,DroidRunJumpActivity.class));
        //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial_wait, menu);
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
*/

package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class TrialWaitActivity extends ActionBarActivity {

    TextView msg;
    Procedure currProcedure;
    GIFView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_wait);

        Intent thisIntent = getIntent();
        currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        final Session currSession = currProcedure.currentSession;
        final boolean prerewardDelay = thisIntent.getBooleanExtra("prerewardDelay", true);
        gifView = (GIFView)findViewById(R.id.gifView);

        msg = (TextView) findViewById(R.id.countdown_msg);

        if(prerewardDelay) {
            final long waitTime = currSession.waitTime.getStartTrialTime();
            new CountDownTimer(waitTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (currSession.isTimerVisible())
                        msg.setText(Integer.toString((int) millisUntilFinished / 1000));
                }

                public void onFinish() {
                    Intent nextAct = new Intent(TrialWaitActivity.this, currSession.selectedGame);
                    nextAct.putExtra("PROCEDURE", currProcedure);

                    long gameTime = currSession.waitTime.getGameTime();

                    new CountDownTimer(gameTime, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Log.i("TWA", "Game time completed");
                            Intent nextAct = new Intent(TrialWaitActivity.this, TrialWaitActivity.class);

                            nextAct.putExtra("PROCEDURE", currProcedure);
                            nextAct.putExtra("prerewardDelay", false);
                            startActivity(nextAct);
                        }
                    }.start();
                    startActivity(nextAct);

                    //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
                }

            }.start();
        }
        else {
            final long waitTime = currSession.waitTime.getEndTrialTime();

            new CountDownTimer(waitTime, 1000) {

            public void onTick(long millisUntilFinished) {
                if (currSession.isTimerVisible())
                    msg.setText(Integer.toString((int) millisUntilFinished / 1000));
            }

            public void onFinish() {

                Intent nextAct = new Intent(TrialWaitActivity.this, TrialMain.class);
                nextAct.putExtra("PROCEDURE", currProcedure);

                startActivity(nextAct);

                //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
            }

        }.start();

        }
    }

    @Override
    public void onBackPressed () {
        // do nothing
    }
}
