package phoenix.delta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import phoenix.delta.DroidRunJumpView.DroidRunJumpThread;


public class DroidRunJumpActivity extends Activity {

    public static final String PREFS_NAME = "DRJPrefsFile";

    DroidRunJumpView drjView;
    DroidRunJumpThread drjThread;
    Session currSession;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent thisIntent = getIntent();
        currSession = (Session) thisIntent.getSerializableExtra("SESSION");
        drjView = (DroidRunJumpView) findViewById(R.id.droidrunjump);

        int gameTime = 2; // to waive the "ready, set, go" time

        if(currSession.getCurrTrialChoice() == Session.WAIT_FOR_GAME)
            gameTime += currSession.getGameTimeDelay();
        else
            gameTime += currSession.getGameTimeInstant();

        new CountDownTimer(gameTime*1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Intent nextAct;
                if(currSession.getCurrTrialChoice() == Session.WAIT_FOR_GAME)
                    nextAct = new Intent(DroidRunJumpActivity.this,TrialMain.class);
                else
                    nextAct = new Intent(DroidRunJumpActivity.this,TrialWaitActivity.class);

                nextAct.putExtra("SESSION", currSession);
                startActivity(nextAct);

                //startActivity(new Intent(TrialWaitActivity.this,TrialMain.class));
            }

        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        drjThread = drjView.getThread();

        // if player wants to quit then reset the game
        if (isFinishing()) {
            drjThread.resetGame();
        }
        else {
            drjThread.pause();
        }

        drjThread.saveGame(editor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore game
        drjThread = drjView.getThread();
        /*SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        drjThread.restoreGame(settings);
        Intent finishedGame = new Intent(DroidRunJumpActivity.this, TrialMain.class);
        finishedGame.putExtra("SESSION", currSession);
        startActivity(finishedGame);*/
    }

    @Override
    public void onBackPressed () {
        // do nothing
        ;
    }
}