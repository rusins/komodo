package com.raitis.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item;
import com.raitis.blocks.Dirt;
import com.raitis.komodoHelpers.AssetLoader;

public class Sand extends Block {

	public Sand(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
	}

	@Override
	public float getDigTime() {
		return 1.2f;
	}

	@Override
	public Item.Type getType() {
		return Item.Type.SAND;
	}

	@Override
	public void dig(int x, int y) {
		new Dirt(gameWorld, player, x, y);
	}

	@Override
	public void update(int x, int y) {
		//TODO
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.sand;
	}

	@Override
	public Type getDrop() {
		return Type.SAND;
	}

	@Override
	public void startUsing(int x, int y) {
		if (gameWorld.world[x][y].getType() == Type.DIRT || gameWorld.world[x][y].getType() == Type.WATER) {
			gameWorld.world[x][y] = new Sand(gameWorld, player);
			gameWorld.updateAdjacentBlocks(x, y);
			player.count[4]--;
		}
		else
			gameWorld.startDigging(Type.SAND);
	}
	
	@Override
	public void stopUsing() {
		gameWorld.stopDigging();
	}

	@Override
	public Type getTool() {
		return Type.SHOVEL;
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
