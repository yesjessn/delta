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

        m_drjView = (DroidRunJumpView) findViewById(R.id.droidrunjump);

        Intent thisIntent = getIntent();
        m_currSession = (Session) thisIntent.getSerializableExtra(Constants.SESSION);
        if(m_currSession == null)
        {
            //if session isn't set up yet, return early and don't execute wait logic
            return;
        }
        if(m_currSession.isTrialNull()){
            //if trial is null, we are in free play mode, so don't setup countdowntimer
            return;
        }

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