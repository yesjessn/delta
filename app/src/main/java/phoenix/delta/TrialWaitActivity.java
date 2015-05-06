package phoenix.delta;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.widget.TextView;


public class TrialWaitActivity extends ActionBarActivity {

    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_wait);

        msg = (TextView) findViewById(R.id.countdown_msg);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                msg.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                startActivity(new Intent(TrialWaitActivity.this,DroidRunJumpActivity.class));
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
