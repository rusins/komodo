package com.raitis.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.komodoHelpers.AssetLoader;

public class Dirt extends Block {

	public Dirt(GameWorld gameWorld, Player player, int x, int y) {
		super(gameWorld, player);
		if (Water.maxAdjacentWaterDepth(x, y) >= 2)
			new Water(gameWorld, player, x, y);
		else
			gameWorld.world[x][y] = this;
		gameWorld.updateAdjacentBlocks(x, y);
	}

	@Override
	public float getDigTime() {
		return -1;
	}

	@Override
	public Type getDrop() {
		return null;
	}

	@Override
	public Type getTool() {
		return null;
	}

	@Override
	public void dig(int x, int y) {
	}

	@Override
	public void update(int x, int y) {
		if (Water.maxAdjacentWaterDepth(x, y) > 1) {
			new Water(gameWorld, player, x, y);
			gameWorld.updateAdjacentBlocks(x, y);
		}
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.dirt;
	}

	@Override
	public void startUsing(int x, int y) {

	}

	@Override
	public void stopUsing() {
	}

	@Override
	public Type getType() {
		return Type.DIRT;
	}


	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
