package com.raitis.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item;
import com.raitis.komodoHelpers.AssetLoader;

public class Water extends Block {

	private int depth = 0, rotation = 0;

	// depth = 9 means not flowing
	// 9 and 8 are sources

	public Water(GameWorld gameWorld, Player player, int x, int y) {
		super(gameWorld, player);
		gameWorld.world[x][y] = this;
		update(x, y);
		gameWorld.updateAdjacentBlocks(x, y);
	}

	public Water(GameWorld gameWorld, Player player, int x, int y, int depth) {
		super(gameWorld, player);
		this.depth = depth;
		gameWorld.world[x][y] = this;
		update(x, y);
		gameWorld.updateAdjacentBlocks(x, y);
	}

	public Water(GameWorld gameWorld, Player player) {
		super(gameWorld, player);
		depth = 9;
	}

	public static int maxAdjacentWaterDepth(int x, int y) {
		int max = 0;
		if (gameWorld.world[x][y + 1].getType() == Type.WATER
				&& ((Water) gameWorld.world[x][y + 1]).getDepth() > max)
			max = ((Water) gameWorld.world[x][y + 1]).getDepth();
		if (gameWorld.world[x - 1][y].getType() == Type.WATER
				&& ((Water) gameWorld.world[x - 1][y]).getDepth() > max)
			max = ((Water) gameWorld.world[x - 1][y]).getDepth();
		if (gameWorld.world[x][y - 1].getType() == Type.WATER
				&& ((Water) gameWorld.world[x][y - 1]).getDepth() > max)
			max = ((Water) gameWorld.world[x][y - 1]).getDepth();
		if (gameWorld.world[x + 1][y].getType() == Type.WATER
				&& ((Water) gameWorld.world[x + 1][y]).getDepth() > max)
			max = ((Water) gameWorld.world[x + 1][y]).getDepth();
		return max;
	}

	public static int minAdjacentWaterDepth(int x, int y) {
		int min = 9;
		if (gameWorld.world[x + 1][y].getType() == Type.WATER
				&& ((Water) gameWorld.world[x][y + 1]).getDepth() < min)
			min = ((Water) gameWorld.world[x][y + 1]).getDepth();
		if (gameWorld.world[x][y + 1].getType() == Type.WATER
				&& ((Water) gameWorld.world[x - 1][y]).getDepth() < min)
			min = ((Water) gameWorld.world[x - 1][y]).getDepth();
		if (gameWorld.world[x - 1][y].getType() == Type.WATER
				&& ((Water) gameWorld.world[x][y - 1]).getDepth() < min)
			min = ((Water) gameWorld.world[x][y - 1]).getDepth();
		if (gameWorld.world[x][y - 1].getType() == Type.WATER
				&& ((Water) gameWorld.world[x + 1][y]).getDepth() < min)
			min = ((Water) gameWorld.world[x + 1][y]).getDepth();
		return min;
	}

	@Override
	public float getDigTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Item.Type getType() {
		return Item.Type.WATER;
	}

	@Override
	public void dig(int x, int y) {

	}

	@Override
	public void update(int x, int y) {
		int temp = maxAdjacentWaterDepth(x, y) - 1;
		if (temp == -1 && depth < 8)
			new Dirt(gameWorld, player, x, y);
		else {
			if (temp == 8)
				temp = 7;
			if (depth < 8 && depth != temp) {
				depth = temp;
				gameWorld.updateAdjacentBlocks(x, y);
			}
			if (depth == 9)
				depth = 8;
			temp = minAdjacentWaterDepth(x, y);
			if (temp < depth) {
				if (((Water) gameWorld.world[x][y + 1]).getDepth() == temp)
					rotation = 0;
				else if (((Water) gameWorld.world[x - 1][y]).getDepth() == temp)
					rotation = 90;
				else if (((Water) gameWorld.world[x][y - 1]).getDepth() == temp)
					rotation = 180;
				else if (((Water) gameWorld.world[x + 1][y]).getDepth() == temp)
					rotation = 270;
			} else {
				if (depth == 8)
					depth = 9;
				else {
					temp = maxAdjacentWaterDepth(x, y);
					if (((Water) gameWorld.world[x][y + 1]).getDepth() == temp)
						rotation = 180;
					else if (((Water) gameWorld.world[x - 1][y]).getDepth() == temp)
						rotation = 270;
					else if (((Water) gameWorld.world[x][y - 1]).getDepth() == temp)
						rotation = 0;
					else if (((Water) gameWorld.world[x + 1][y]).getDepth() == temp)
						rotation = 90;
				}
			}
		}
	}

	@Override
	public TextureRegion getTexture(float runTime) {
		switch (depth) {
		case 9:
			return AssetLoader.water[0];
		default:
			return AssetLoader.waterAnimation.getKeyFrame(runTime, true);
		}
	}

	@Override
	public Type getDrop() {
		return null;
	}

	@Override
	public void startUsing(int x, int y) {

	}

	@Override
	public void stopUsing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getTool() {
		return Type.BUCKET;
	}

	@Override
	public TextureRegion getTexture() {
		return AssetLoader.water[0];
	}

	public int getDepth() {
		return depth;
	}

	public int getRotation() {
		return rotation;
	}

}
