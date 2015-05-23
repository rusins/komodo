package com.raitis.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.*;
import com.raitis.komodoHelpers.AssetLoader;

public class Cactus extends Block {

	public enum GrowthState {
		SMALL, NORMAL, FLOWER
	}

	GrowthState growthState;

	public Cactus(GameWorld gameWorld, Player player, GrowthState growthState) {
		super(gameWorld, player);
		this.growthState = growthState;
	}

	@Override
	public float getDigTime() {
		// TODO Auto-generated method stub
		return 0.5f;
	}

	@Override
	public Item.Type getType() {
		return Item.Type.CACTUS;
	}

	@Override
	public void dig(int x, int y) {
		gameWorld.world[x][y] = new Sand(gameWorld, player);
	}

	@Override
	public void startUsing(int x, int y) {
		if (gameWorld.world[x][y].getType() == Type.SAND) {
			gameWorld.world[x][y] = new Cactus(gameWorld, player,
					GrowthState.NORMAL);
			player.count[3]--;
			if (gameWorld.getDragon().isPlayerVisible())
				gameWorld.getDragon().updatePath();
		} else
			gameWorld.startDigging(Type.CACTUS);
	}

	@Override
	public void stopUsing() {
		gameWorld.stopDigging();
	}

	@Override
	public void update(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public TextureRegion getTexture() {
		switch (growthState) {
		case SMALL:
			return AssetLoader.smallCactus;
		case NORMAL:
			return AssetLoader.cactus;
		case FLOWER:
			return AssetLoader.flowerCactus;
		default:
			return AssetLoader.cactus;
		}
	}

	@Override
	public Type getDrop() {
		return Type.CACTUS;
	}

	@Override
	public Type getTool() {
		return Type.ANYTHING;
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public float getRad() {
		switch (growthState) {
		case SMALL:
			return 0.2f;
		case NORMAL:
			return 0.4f;
		case FLOWER:
			return 0.4f;
		default:
			return 0.4f;
		}
	}

}
