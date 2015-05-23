package com.raitis.structures;

import com.raitis.blocks.Block;
import com.raitis.items.Item;
import com.raitis.blocks.Water;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld;
import java.util.Random;

public class Lake {

	public static int adjacentWaterCount(GameWorld gameWorld, int x, int y) {
		int count = 0;
		if (gameWorld.world[x][y].getType() == Item.Type.WATER)
			return 0;
		if (gameWorld.world[x + 1][y].getType() == Item.Type.WATER)
			++count;
		if (gameWorld.world[x][y + 1].getType() == Item.Type.WATER)
			++count;
		if (gameWorld.world[x - 1][y].getType() == Item.Type.WATER)
			++count;
		if (gameWorld.world[x][y - 1].getType() == Item.Type.WATER)
			++count;
		return count;
	}
	
	public static void makeLake(GameWorld gameWorld, Player player, int x, int y, Random random) {
		if (x > 2 && x < gameWorld.getWorldSize()-2 && y > 2
				&& y < gameWorld.getWorldSize()-2) {
			gameWorld.world[x][y] = new Water(gameWorld, player);
			if (random.nextInt((4 - adjacentWaterCount(gameWorld, x + 1, y))*2+1) == 0)
				makeLake(gameWorld, player, x + 1, y, random);
			if (random.nextInt((4 - adjacentWaterCount(gameWorld, x, y+1))*2+1) == 0)
				makeLake(gameWorld, player, x, y + 1, random);
			if (random.nextInt((4 - adjacentWaterCount(gameWorld, x - 1, y))*2+1) == 0)
				makeLake(gameWorld, player, x - 1, y, random);
			if (random.nextInt((4 - adjacentWaterCount(gameWorld, x, y - 1))*2+1) == 0)
				makeLake(gameWorld, player, x, y - 1, random);
		}
	}
}
