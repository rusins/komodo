package com.raitis.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.blocks.Dirt;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.komodoHelpers.AssetLoader;

public class Bucket extends Item{

	public Bucket(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startUsing(int x, int y) {
		if (gameWorld.world[x][y].getType() == Type.WATER) {
			new Dirt(gameWorld, player, x, y);
			player.count[1]--;
			player.count[2]++;
		}
		else
			gameWorld.startDigging(Type.BUCKET);
	}

	@Override
	public void stopUsing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.BUCKET;
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureRegion getTexture() {
		// TODO Auto-generated method stub
		return AssetLoader.bucket;
	}

}
