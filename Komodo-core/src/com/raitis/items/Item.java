package com.raitis.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;

public abstract class Item {
	protected static GameWorld gameWorld;
	protected static Player player;

	public enum Type {
		ANYTHING, SHOVEL, BUCKET, WATER_BUCKET, SAND, WATER, CACTUS, CHEST, DIRT
	}
	
	public Item(GameWorld gameWorld, Player player) {
		Item.gameWorld = gameWorld;
		Item.player = player;
	}

	public abstract void startUsing(int x, int y);
	public abstract void stopUsing();
	public abstract Type getType();
	public abstract TextureRegion getTexture(float runTime);
	public abstract TextureRegion getTexture();
}
