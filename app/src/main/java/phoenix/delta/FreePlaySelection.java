package phoenix.delta;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import br.usp.ime.retrobreaker.game.Game;
import droidrunjump.DroidRunJumpActivity;

public class FreePlaySelection extends ActionBarActivity{

    RadioButton playGame1Btn, playGame2Btn, playGame3Btn, playGame4Btn;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_freeplay_selection);

        Intent thisIntent = getIntent();
        final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");

        playGame1Btn = (RadioButton) findViewById(R.id.play_game1_btn);

        playGame2Btn = (RadioButton) findViewById(R.id.play_game2_btn);

        playGame3Btn = (RadioButton) findViewById(R.id.play_game3_btn);

        playGame4Btn = (RadioButton) findViewById(R.id.play_game4_btn);

        continue_btn = (Button)findViewById(R.id.btn_continue);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = null;
                Log.i("FPS", String.format("{%s, %s, %s, %s}", playGame1Btn.isChecked(), playGame2Btn.isChecked(), playGame3Btn.isChecked(), playGame4Btn.isChecked()));
                if (playGame1Btn.isChecked()) {
                    game = new Intent(FreePlaySelection.this, DroidRunJumpActivity.class);
                } else if (playGame2Btn.isChecked()) {
                    game = new Intent(FreePlaySelection.this, dev.emmaguy.fruitninja.ui.MainActivity.class);
                } else if (playGame3Btn.isChecked()) {
                    game = new Intent(FreePlaySelection.this, br.usp.ime.retrobreaker.GameActivity.class);
                    Game.State.setDifficult(2 /* normal */);
                    Game.State.enableSoundEffects(true);
                } else if (playGame4Btn.isChecked()) {
                    game = new Intent(FreePlaySelection.this, com.mystery_of_orient_express.game.android.AndroidLauncher.class);
                } else {
                    Toast.makeText(FreePlaySelection.this.getApplicationContext(), "Please select a game", Toast.LENGTH_SHORT);
                    return;
                }
                game.putExtra("PROCEDURE", currProcedure);
                game.putExtra("IsFreePlay", true);
                startActivity(game);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent thisIntent = getIntent();
        final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
        Intent gameSelection = new Intent(FreePlaySelection.this, SessionStartActivity.class);
        gameSelection.putExtra("PROCEDURE", currProcedure);
        startActivity(gameSelection);
    }

}
