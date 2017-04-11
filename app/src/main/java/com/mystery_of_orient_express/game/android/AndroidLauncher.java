package com.mystery_of_orient_express.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mystery_of_orient_express.game.MysteryOfOrientExpress;

import phoenix.delta.FreePlaySelection;
import phoenix.delta.Procedure;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MysteryOfOrientExpress(), config);
	}

	@Override
	public void onBackPressed() {
		Intent thisIntent = getIntent();
		final Procedure currProcedure = (Procedure) thisIntent.getSerializableExtra("PROCEDURE");
		if (thisIntent.getBooleanExtra("IsFreePlay", false)) {
			Intent gameSelection = new Intent(AndroidLauncher.this, FreePlaySelection.class);
			gameSelection.putExtra("PROCEDURE", currProcedure);
			startActivity(gameSelection);
		} else {
			//do nothing
		}
	}
}
