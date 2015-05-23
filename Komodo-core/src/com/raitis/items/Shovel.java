package com.raitis.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameWorld.GameWorld;
import com.raitis.komodoHelpers.AssetLoader;
import com.raitis.gameObjects.Player;

public class Shovel extends Item{

	public Shovel(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
	}

	@Override
	public void startUsing(int x, int y) {
		gameWorld.startDigging(Type.SHOVEL);
	}
	
	@Override
	public void stopUsing() {
		gameWorld.stopDigging();
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.shovel;
	}

	@Override
	public Type getType() {
		return Type.SHOVEL;
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
