package com.raitis.komodo;

import com.badlogic.gdx.Game;
import com.raitis.screens.GameScreen;
import com.raitis.komodoHelpers.AssetLoader;;

public class Komodo extends Game{

	@Override
	public void create() {
		System.out.println("Loading assets");
		AssetLoader.load();
		System.out.println("Komodo Game created!");
		setScreen(new GameScreen());
	}
	
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}
