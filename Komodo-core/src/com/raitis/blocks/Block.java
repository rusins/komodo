package com.raitis.blocks;

import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item;

public abstract class Block extends Item{

	public Block(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
	}
	
	public abstract float getDigTime();
	public abstract Type getDrop();
	public abstract Type getTool(); //return needed tool to dig
	public abstract void dig(int x, int y);
	public abstract void update(int x, int y);
}
