package phoenix.delta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

public class DroidRunJumpActivity extends Activity
{
    private DroidRunJumpView m_drjView;
    private DroidRunJumpThread m_drjThread;
    private Session m_currSession;

    @Override
    public void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.main);

        Intent thisIntent = getIntent();
        m_currSession = (Session) thisIntent.getSerializableExtra(Constants.SESSION);
        if(m_currSession == null)
        {
            //if session isn't set up yet, return early and don't execute wait logic
            return;
        }
        m_drjView = (DroidRunJumpView) findViewById(R.id.droidrunjump);

        int gameTime = 2; // to waive the "ready, set, go" time

        if(m_currSession.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME)
        {
            gameTime += m_currSession.getGameTimeDelay();
        }
        else
        {
            gameTime += m_currSession.getGameTimeInstant();
        }

        new CountDownTimer(gameTime*1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {

            }

            public void onFinish()
            {

                Intent nextAct;
                if(m_currSession.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME)
                {
                    nextAct = new Intent(DroidRunJumpActivity.this, TrialMain.class);
                }
                else
                {
                    nextAct = new Intent(DroidRunJumpActivity.this, TrialWaitActivity.class);
                }

                nextAct.putExtra(Constants.SESSION, m_currSession);
                startActivity(nextAct);
            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        m_drjThread = m_drjView.getThread();

        // if player wants to quit then reset the game
        if (isFinishing()) {
            m_drjThread.resetGame();
        }
        else {
            m_drjThread.pause();
        }

        m_drjThread.saveGame(editor);
        editor.apply();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_drjThread = m_drjView.getThread();
    }

    @Override
    public void onBackPressed ()
    {
        // do nothing
    }
}