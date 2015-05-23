package com.raitis.komodoHelpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.raitis.gameObjects.Joystick;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameRenderer;
import com.raitis.gameWorld.GameWorld;
import com.raitis.gameWorld.GameWorld.GameState;

public class InputHandler implements InputProcessor {

	private Joystick joystick;
	private Player player;
	private int width, height;
	private GameRenderer renderer;
	private GameWorld gameWorld;
	private int originalZoom;
	private int zoom;
	private int fingerCount = 0;

	public InputHandler(Joystick joystick, Player player, int width,
			int height, GameRenderer renderer, GameWorld gameWorld, int zoom) {
		this.joystick = joystick;
		this.player = player;
		this.width = width;
		this.height = height;
		this.renderer = renderer;
		this.gameWorld = gameWorld;
		this.zoom = zoom;
		originalZoom = zoom;
	}

	public void setZoom(int zoom) {
		if (zoom >= 64 && zoom <= 256) {
			gameWorld.setMessage("Setting zoom level to " + zoom);
			this.zoom = zoom;
			renderer.setZoom(zoom);
			gameWorld.setZoom(zoom);
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (gameWorld.gameState == GameState.INTRO)
			gameWorld.gameState = GameState.RUNNING;
		if (keycode == Keys.DOWN || keycode == Keys.S)
			player.setMovingDown(true);
		else if (keycode == Keys.UP || keycode == Keys.W)
			player.setMovingUp(true);
		else if (keycode == Keys.LEFT || keycode == Keys.A)
			player.setMovingLeft(true);
		else if (keycode == Keys.RIGHT || keycode == Keys.D)
			player.setMovingRight(true);
		else if (keycode == Keys.ENTER || keycode == Keys.SPACE)
			player.startUsing();
		joystick.setVector(player.getVelocity().cpy().scl(500));

		// Just 4 fun
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.DOWN || keycode == Keys.S)
			player.setMovingDown(false);
		else if (keycode == Keys.UP || keycode == Keys.W)
			player.setMovingUp(false);
		else if (keycode == Keys.LEFT || keycode == Keys.A)
			player.setMovingLeft(false);
		else if (keycode == Keys.RIGHT || keycode == Keys.D)
			player.setMovingRight(false);
		else if (keycode == Keys.ENTER || keycode == Keys.SPACE)
			player.stopUsing();
		joystick.setVector(player.getVelocity().cpy().scl(500));
		// Just 4 fun
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		switch (character) {
		case '1':
			player.setSelected(0);
			break;
		case '2':
			player.setSelected(1);
			break;
		case '3':
			player.setSelected(2);
			break;
		case '4':
			player.setSelected(3);
			break;
		case '5':
			player.setSelected(4);
			break;
		case 'f':
			renderer.toggleDebug();
			break;
		case '=':
			setZoom(zoom * 2);
			break;
		case '-':
			setZoom(zoom / 2);
			break;
		case '0':
			setZoom(originalZoom);
			break;
		}
		return true;
	}

	// In future for mobile
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		fingerCount++;
		if (gameWorld.gameState == GameState.INTRO)
			gameWorld.gameState = GameState.RUNNING;
		else if (gameWorld.gameState == GameState.DEAD) {
			if (screenX > width * 3 / 4 && screenX < width * 11 / 12
					&& screenY > height * 2 / 3 && screenY < height * 19 / 24)
				gameWorld.reset();
		}
		if (screenX < width / 2)
			joystick.press(screenX, screenY);
		else if (screenX > width - zoom && screenY > height - zoom)
			player.setSelected(4);
		else if (screenX > width - zoom * 2 && screenY > height - zoom)
			player.setSelected(3);
		else if (screenX > width - zoom * 3 && screenY > height - zoom)
			player.setSelected(2);
		else if (screenX > width - zoom * 4 && screenY > height - zoom)
			player.setSelected(1);
		else if (screenX > width - zoom * 5 && screenY > height - zoom)
			player.setSelected(0);
		else
			player.startUsing();
		return true; // We handled the input method
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		fingerCount--;
		if (screenX > width / 2)
			player.stopUsing();
		if (fingerCount == 0)
			joystick.setPressed(false);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (screenX < width / 2 || fingerCount == 1)
			joystick.touch(screenX, screenY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		player.incSelected(amount);
		return true;
	}

}
