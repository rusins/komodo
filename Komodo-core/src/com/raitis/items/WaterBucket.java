package com.raitis.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.blocks.Dirt;
import com.raitis.blocks.Water;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item.Type;
import com.raitis.komodoHelpers.AssetLoader;

public class WaterBucket extends Item {

	public WaterBucket(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startUsing(int x, int y) {
		if (gameWorld.world[x][y].getType() == Type.DIRT || gameWorld.world[x][y].getType() == Type.WATER) {
			new Water(gameWorld, player, x, y, 8);
			player.count[2]--;
			player.count[1]++;
		}
		else
			gameWorld.startDigging(Type.WATER_BUCKET);
	}

	@Override
	public void stopUsing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.waterBucket;
	}

}
