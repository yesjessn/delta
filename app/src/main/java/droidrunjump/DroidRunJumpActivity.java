package droidrunjump;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import phoenix.delta.FreePlaySelection;
import phoenix.delta.Procedure;
import phoenix.delta.R;
import phoenix.delta.Session;

public class DroidRunJumpActivity extends Activity {
    private DroidRunJumpView m_drjView;
    private DroidRunJumpThread m_drjThread;

    MediaPlayer alertSnd;

    @Override
    public void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.main);

        m_drjView = (DroidRunJumpView) findViewById(R.id.droidrunjump);
        alertSnd = MediaPlayer.create(this, R.raw.save);
        alertSnd.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences(DroidConstants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        m_drjThread = m_drjView.getThread();

        // if player wants to quit then reset the game
        if (isFinishing()) {
            m_drjThread.resetGame();
        } else {
            m_drjThread.pause();
        }

        m_drjThread.saveGame(editor);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_drjThread = m_drjView.getThread();
    }

    @Override
    public void onBackPressed() {
        Intent thisIntent = getIntent();
        final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        if (thisIntent.getBooleanExtra("IsFreePlay", false)) {
            Intent gameSelection = new Intent(DroidRunJumpActivity.this, FreePlaySelection.class);
            gameSelection.putExtra("PROCEDURE", currProcedure);
            startActivity(gameSelection);
        } else {
            //do nothing
        }
    }
}