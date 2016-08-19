package phoenix.delta;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class FreePlaySelection extends ActionBarActivity{

    Button playGame1Btn, playGame2Btn, playGame3Btn, playGame4Btn, continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_freeplay_selection);

        Intent thisIntent = getIntent();
        final Session currSession = (Session) thisIntent.getSerializableExtra("SESSION");

        playGame1Btn = (Button) findViewById(R.id.play_game1_btn);
        playGame1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = new Intent(FreePlaySelection.this, DroidRunJumpActivity.class);
                game.putExtra("SESSION", currSession);
                startActivity(game);

            }
        });

        playGame2Btn = (Button) findViewById(R.id.play_game2_btn);
        playGame2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(FreePlaySelection.this, dev.emmaguy.fruitninja.ui.MainActivity.class);
                game.putExtra("SESSION", currSession);
                startActivity(game);
            }
        });

        playGame3Btn = (Button) findViewById(R.id.play_game3_btn);
        playGame3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = new Intent(FreePlaySelection.this, DroidRunJumpActivity.class);
                game.putExtra("SESSION", currSession);
                startActivity(game);

            }
        });

        playGame4Btn = (Button) findViewById(R.id.play_game4_btn);
        playGame4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = new Intent(FreePlaySelection.this, DroidRunJumpActivity.class);
                game.putExtra("SESSION", currSession);
                startActivity(game);

            }
        });

        continue_btn = (Button)findViewById(R.id.btn_continue);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent game = new Intent(FreePlaySelection.this, GameSelection.class);
                game.putExtra("SESSION", currSession);
                startActivity(game);
            }
        });

    }

}
