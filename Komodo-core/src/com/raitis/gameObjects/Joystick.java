package com.raitis.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.raitis.komodoHelpers.AssetLoader;

public class Joystick {

	private Vector2 origin, coords, vector;
	private boolean pressed = false;
	private int middle;
	private Player player;
	public TextureRegion texture;

	public Joystick(Player player, int size, int originX, int originY) {
		this.player = player;
		middle = size / 2;
		origin = new Vector2(originX, originY);
		coords = new Vector2(origin);
		texture = new TextureRegion(AssetLoader.circleTextureRegion);                                              
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
		if (!pressed) {
			coords.x = origin.x;
			coords.y = origin.y;
			texture = AssetLoader.circleTextureRegion;
			player.updateMovement(new Vector2(0, 0));
		} else
			texture = AssetLoader.doubleCircleTextureRegion;

	}

	public void press(int x, int y) {
		int xs = (int) (x - origin.x), ys = (int) (y - origin.y);
		if (Math.sqrt(xs * xs + ys * ys) < middle * 3) {
			setPressed(true);
			touch(x, y);
		}
	}

	public void touch(int x, int y) {
		if (pressed) {
			coords.x = x;
			coords.y = y;
			vector = coords.cpy().sub(origin);
			vector.limit(middle * 3);
			coords = origin.cpy().add(vector);
			player.updateMovement(vector);
		}
	}

	public int getOriginX() {
		return (int) origin.x;
	}

	public void setOriginX(int originX) {
		origin.x = originX;
	}

	public int getOriginY() {
		return (int) origin.y;
	}

	public void setOriginY(int originY) {
		origin.y = originY;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public int getSize() {
		return middle * 2;
	}

	public void setSize(int size) {
		middle = size / 2;
	}

	public int getMiddle() {
		return middle;
	}

	public void setMiddle(int middle) {
		this.middle = middle;
	}
	
	public int getX() {
		return (int) (coords.x - middle);
	}
	
	public int getY() {
		return (int) (coords.y - middle);
	}
	
	public void setVector(Vector2 v) {
		vector = v;
		vector.limit(middle * 3);
		coords = origin.cpy().add(vector);
	}
}
