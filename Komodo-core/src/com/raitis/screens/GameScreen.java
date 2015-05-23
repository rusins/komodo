package com.raitis.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.komodoHelpers.AssetLoader;
import com.raitis.komodoHelpers.InputHandler;
//import com.badlogic.gdx.graphics.GL20;

import com.raitis.gameObjects.Joystick;
import com.raitis.gameWorld.GameRenderer;
import com.raitis.gameWorld.GameWorld;

public class GameScreen implements Screen{

	private GameWorld gameWorld;
	private GameRenderer renderer;
	private Joystick joystick;
	private int zoom; //Default? pixels per block
	private int width, height, midX, midY;
	private InputHandler inputHandler;
	
	private float runTime;
	
	public GameScreen() {
		if (Gdx.app.getType() == ApplicationType.Android)
			zoom = 128;
		else
			zoom = 64;
		System.out.println("GameScreen attached");
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		midX = width / 2;
		midY = height / 2;
		
		gameWorld = new GameWorld(width, height, midX, midY, zoom);
		joystick = new Joystick(gameWorld.getPlayer(), zoom, width / 5, height - width / 5);
		renderer = new GameRenderer(gameWorld, width, height, midX, midY, zoom, joystick);
		inputHandler = new InputHandler(joystick, gameWorld.getPlayer(), width, height, renderer, gameWorld, zoom);
		Gdx.input.setInputProcessor(inputHandler);
	}
	
	@Override
	public void render(float delta) {
		runTime += delta;
	    gameWorld.update(delta);
	    renderer.render(delta, runTime);
	}

	@Override
	public void resize(int width, int height) {
	    System.out.println("GameScreen - resizing");
	    midX = Gdx.graphics.getWidth() / 2;
		midY = Gdx.graphics.getHeight() / 2;
		gameWorld.setWidth(width);
		gameWorld.setHeight(height);
		joystick.setOriginX(width / 5);
		joystick.setOriginY(height - width / 5);
		renderer.setWidth(width);
		renderer.setHeight(height);
		inputHandler.setWidth(width);
		inputHandler.setHeight(height);
	}

	@Override
	public void show() {
	    System.out.println("GameScreen - show called");	
	}

	@Override
	public void hide() {
		System.out.println("GameScreen - hide called");
	}

	@Override
	public void pause() {
		System.out.println("GameScreen - pause called");
		gameWorld.pause();
	}

	@Override
	public void resume() {
		System.out.println("GameScreen - resume called");
		gameWorld.resume();
	}

	@Override
	public void dispose() {
		//Leave blank
	}

}
