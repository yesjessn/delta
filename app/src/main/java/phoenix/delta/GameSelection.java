package phoenix.delta;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import br.usp.ime.retrobreaker.game.Game;
import droidrunjump.DroidRunJumpActivity;


public class GameSelection extends ActionBarActivity {

    RadioButton  playGame1Btn, playGame2Btn, playGame3Btn, playGame4Btn;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game_selection);

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
                if (playGame1Btn.isChecked()) {
                    currProcedure.startNewSession();
                    currProcedure.currentSession.selectedGame = DroidRunJumpActivity.class;
                } else if (playGame2Btn.isChecked()) {
                    currProcedure.startNewSession();
                    currProcedure.currentSession.selectedGame = dev.emmaguy.fruitninja.ui.MainActivity.class;
                } else if (playGame3Btn.isChecked()) {
                    currProcedure.startNewSession();
                    currProcedure.currentSession.selectedGame = br.usp.ime.retrobreaker.GameActivity.class;
                    Game.State.setDifficult(2 /* normal */);
                    Game.State.enableSoundEffects(true);
                } else if (playGame4Btn.isChecked()) {
                    currProcedure.startNewSession();
                    currProcedure.currentSession.selectedGame = com.mystery_of_orient_express.game.android.AndroidLauncher.class;
                } else {
                    Toast.makeText(GameSelection.this.getApplicationContext(), "Please select a game", Toast.LENGTH_SHORT);
                    return;
                }

                Intent game = new Intent(GameSelection.this, TrialMain.class);
                game.putExtra("PROCEDURE", currProcedure);
                startActivity(game);
            }
        });

    }

}

