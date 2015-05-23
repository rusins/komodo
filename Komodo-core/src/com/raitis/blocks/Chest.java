package com.raitis.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item;
import com.raitis.komodoHelpers.AssetLoader;

public class Chest extends Block{

	private Item item;
	
	public Chest(GameWorld gameWorld, Player player, Item item) {
		super(gameWorld, player);
		this.item = item;
	}

	@Override
	public float getDigTime() {
		return 0;
	}

	@Override
	public Item.Type getType() {
		// TODO Auto-generated method stub
		return Item.Type.CHEST;
	}

	@Override
	public void dig(int x, int y) {
		gameWorld.world[x][y] =  new Sand(gameWorld, player);
	}

	@Override
	public void update(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.chest;
	}

	@Override
	public Type getDrop() {
		return item.getType();
	}

	@Override
	public void startUsing(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void stopUsing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getTool() {
		return null;
	}


	@Override
	public TextureRegion getTexture(float runTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
