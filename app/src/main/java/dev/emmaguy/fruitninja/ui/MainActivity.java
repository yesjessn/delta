package dev.emmaguy.fruitninja.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;
import dev.emmaguy.fruitninja.ui.MainMenuFragment.OnMainMenuButtonClicked;
import phoenix.delta.FreePlaySelection;
import phoenix.delta.Procedure;
import phoenix.delta.R;

public class MainActivity extends FragmentActivity implements OnMainMenuButtonClicked, OnGameOver {

	MediaPlayer alertSnd;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main_fn);
	alertSnd = MediaPlayer.create(this, R.raw.save);
	alertSnd.start();

	onPlayButtonClicked();
    }

    private void showMainMenu() {
	MainMenuFragment fragment = new MainMenuFragment();
	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, fragment, "MainMenu");
	transaction.addToBackStack("MainMenu");
	transaction.commit();
    }

    @Override
    public void onPlayButtonClicked() {
	GameFragment gameFragment = new GameFragment();

	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, gameFragment, "Game");
	transaction.addToBackStack("Game");
	transaction.commit();
    }

    @Override
    public void onGameOver(int score) {
	ResultsFragment resultsFragment = new ResultsFragment();
	resultsFragment.setScore(score);

	FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
	transaction.replace(R.id.fragment_container, resultsFragment, "Results");
	transaction.commit();
    }
    
    @Override
    public void onBackPressed() {
		Intent thisIntent = getIntent();
		final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
		if (thisIntent.getBooleanExtra("IsFreePlay", false)) {
			Intent gameSelection = new Intent(MainActivity.this, FreePlaySelection.class);
			gameSelection.putExtra("PROCEDURE", currProcedure);
			startActivity(gameSelection);
		} else {
			//do nothing
		}
	}
}