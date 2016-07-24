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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class TrialWaitActivity extends ActionBarActivity {

    TextView msg;
    Session currSession;
    GIFView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_trial_wait);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");
        gifView = (GIFView)findViewById(R.id.gifView);

        int waitTime = currSession.getWaitTime();

        msg = (TextView) findViewById(R.id.countdown_msg);
        new CountDownTimer(waitTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(currSession.isTimerVisible())
                    msg.setText(Integer.toString((int)millisUntilFinished / 1000));
            }

            public void onFinish() {

                Intent nextAct;
                if(currSession.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME)
                    nextAct = new Intent(TrialWaitActivity.this,DroidRunJumpActivity.class);
                else
                    nextAct = new Intent(TrialWaitActivity.this,TrialMain.class);

                nextAct.putExtra("SESSION", currSession);
                startActivity(nextAct);

                //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
            }

        }.start();
    }

    @Override
    public void onBackPressed () {
        // do nothing
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
