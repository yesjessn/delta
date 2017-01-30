package phoenix.delta;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class SessionStartActivity extends ActionBarActivity {
    Button freePlayBtn, startSessionBtn, cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_session_start);

        Intent thisIntent = getIntent();
        final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");

        freePlayBtn = (Button) findViewById(R.id.free_play_btn);
        freePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent gameSelection = new Intent(SessionStartActivity.this, FreePlaySelection.class);
                    gameSelection.putExtra("PROCEDURE", currProcedure);
                    startActivity(gameSelection);

            }
        });

        startSessionBtn = (Button) findViewById(R.id.start_btn);
        startSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSession = new Intent(SessionStartActivity.this, GameSelection.class);
                startSession.putExtra("PROCEDURE", currProcedure);
                currProcedure.startNewSession();
                startActivity(startSession);
            }
        });

        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent nextAct = new Intent(SessionStartActivity.this, SessionPrep.class);
                nextAct.putExtra("PROCEDURE", currProcedure);
                startActivity(nextAct);

            }
        });


    }
}