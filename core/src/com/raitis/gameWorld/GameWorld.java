package com.raitis.gameWorld;

import com.badlogic.gdx.math.Vector2;
import com.raitis.gameObjects.Dragon;
import com.raitis.gameObjects.Player;
import com.raitis.blocks.*;
import com.raitis.items.*;
import com.raitis.items.Item.Type;
import com.raitis.structures.Lake;

import java.util.Random;

public class GameWorld {

	private final int worldSize = 128;
	private final int cactusChance = 15;
	private final int smallCactusChance = 5;
	private final int flowerCactusChance = 60;
	private final int chestChance = 800;
	private final int lakeChance = 400;
	private final float waterCurrent = 0.5f;
	private final int dragonSpawnBounds = 40;
	private final float dragonSightDistance = 5;

	public enum GameState {
		INTRO, RUNNING, PAUSED, DEAD
	}

	private Dragon dragon;
	private Player player;
	public Block[][] world;
	private Random random;
	private Vector2 cam; // camera center position (where the player gets to
							// move
	private Vector2 lookingAt;
	private float xBounds, yBounds; // Bounds for where the player can move
	private int width, height, midX, midY, zoom;
	private Block selectedBlock;
	private float digTime;
	private boolean digging = false;
	private String message;
	private float messageTime = 0;
	public GameState gameState = GameState.INTRO;

	public GameWorld(int width, int height, int midX, int midY, int zoom) {
		random = new Random();
		player = new Player(this, worldSize / 2, worldSize / 2, 0, 0.4f);
		dragon = new Dragon(this, player, random.nextInt(worldSize - 2) + 1,
				random.nextInt(worldSize - 2) + 1, 0.9f, 1.6f);
		cam = new Vector2(worldSize / 2, worldSize / 2);
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.midX = midX;
		this.midY = midY;
		xBounds = width / 4 / zoom;
		yBounds = height / 4 / zoom;

		new Water(this, player);
		// In case we get no water, this is so the game doesn't crash
		// when calling static methods

		// World generation!
		world = new Block[worldSize][worldSize];
		for (int i = 0; i < worldSize; ++i) {
			for (int j = 0; j < worldSize; ++j) {
				if (random.nextInt(cactusChance) == 0)
					if (random.nextInt(smallCactusChance) == 0)
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.SMALL);
					else if (random.nextInt(flowerCactusChance) == 0)
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.FLOWER);
					else
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.NORMAL);
				else if (random.nextInt(chestChance) == 0)
					if (random.nextInt(2) == 0)
						world[i][j] = new Chest(this, player, new Shovel(this,
								player));
					else
						world[i][j] = new Chest(this, player, new Bucket(this,
								player));
				else
					world[i][j] = new Sand(this, player);
			}
		}

		// Structure generation!
		for (int i = 0; i < worldSize; ++i) {
			for (int j = 0; j < worldSize; ++j) {
				if (random.nextInt(lakeChance) == 0)
					Lake.makeLake(this, player, i, j, random);
			}
		}

		// Cacti wall
		for (int i = 0; i < worldSize; ++i)
			world[i][0] = new Cactus(this, player, Cactus.GrowthState.NORMAL);
		for (int i = 0; i < worldSize; ++i)
			world[i][worldSize - 1] = new Cactus(this, player,
					Cactus.GrowthState.NORMAL);
		for (int i = 1; i < worldSize - 1; ++i)
			world[0][i] = new Cactus(this, player, Cactus.GrowthState.NORMAL);
		for (int i = 1; i < worldSize - 1; ++i)
			world[worldSize - 1][i] = new Cactus(this, player,
					Cactus.GrowthState.NORMAL);

		while (world[(int) (player.position.x)][(int) (player.position.y + player
				.getRad())].getType() == Type.CACTUS)
			player.position.x += 1;
		selectedBlock = selectNew();
	}

	public void update(float delta) {
		if (gameState == GameState.RUNNING) {
			player.update(delta);

			// collision detection
			// Water detection
			if (world[(int) (player.position.x)][(int) (player.position.y)]
					.getType() == Item.Type.WATER) {
				Vector2 v = new Vector2();
				if (((Water) world[(int) (player.position.x)][(int) (player.position.y)])
						.getDepth() != 9) {
					switch (((Water) world[(int) (player.position.x)][(int) (player.position.y)])
							.getRotation()) {
					case 0:
						v.y = waterCurrent;
						break;
					case 90:
						v.x = -1 * waterCurrent;
						break;
					case 180:
						v.y = -1 * waterCurrent;
						break;
					case 270:
						v.x = waterCurrent;
						break;
					}
				}
				player.inWater(true, v);
			} else
				player.inWater(false, null);

			// Chest detection
			if (world[(int) (player.position.x)][(int) (player.position.y)]
					.getType() == Item.Type.CHEST) {
				player.giveItem(world[(int) (player.position.x + player
						.getRad())][(int) (player.position.y + player.getRad())]
						.getDrop());
				world[(int) (player.position.x)][(int) (player.position.y + player
						.getRad())] = new Sand(this, player);
			}

			// Cacti collision detection
			/*
			 * if (world[(int) (player.position.x)][(int) (player.position.y)]
			 * .getType() == Item.Type.CACTUS || world[(int) (player.position.x
			 * + player.getWidth())][(int) (player.position.y)] .getType() ==
			 * Item.Type.CACTUS || world[(int) (player.position.x)][(int)
			 * (player.position.y + player .getHeight())].getType() ==
			 * Item.Type.CACTUS || world[(int) (player.position.x +
			 * player.getWidth())][(int) (player.position.y + player
			 * .getHeight())].getType() == Item.Type.CACTUS)
			 * player.position.add(player.getVelocity().cpy().scl(-1 * delta));
			 */

			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					if (world[(int) player.position.x + i][(int) player.position.y
							+ j].getType() == Type.CACTUS)
						if (Math.sqrt(Math.pow((player.position.x % 1)
								- (i + 0.5), 2)
								+ Math.pow((player.position.y % 1) - (j + 0.5),
										2)) < player.getRad()
								+ ((Cactus) world[(int) player.position.x + i][(int) player.position.y
										+ j]).getRad())
							player.position.add(player.getVelocity().cpy()
									.scl(-1 * delta));
				}
			}

			// Move camera to follow player
			if (player.position.x < cam.x - xBounds)
				cam.x = player.position.x + xBounds;
			else if (player.position.x > cam.x + xBounds)
				cam.x = player.position.x - xBounds;

			if (player.position.y < cam.y - yBounds)
				cam.y = player.position.y + yBounds;
			else if (player.position.y > cam.y + yBounds)
				cam.y = player.position.y - yBounds;

			// Don't allow to see world border
			if (cam.x - midX / zoom < 0)
				cam.x = midX / zoom;
			else if (cam.x + midX / zoom > worldSize)
				cam.x = worldSize - midX / zoom;
			if (cam.y - midY / zoom < 1)
				cam.y = 1 + midY / zoom;
			else if (cam.y + midY / zoom > worldSize - 1)
				cam.y = worldSize - 1 - midY / zoom;

			selectedBlock = selectNew();
			if (digging) {
				digTime += delta;
				if (digTime >= selectedBlock.getDigTime()) {
					stopDigging();
					player.giveItem(selectedBlock.getDrop());
					selectedBlock.dig((int) lookingAt.x, (int) lookingAt.y);
					selectedBlock = world[(int) lookingAt.x][(int) lookingAt.y];
					if (dragon.isPlayerVisible()
							&& selectedBlock.getType() == Type.SAND)
						dragon.updatePath();
				}
			}

			if (dragon.isPlayerVisible() == false) {
				if (dragonInSight())
					dragon.setPlayerVisible(delta);
			}

			dragon.update(delta);

			// Water detection
			if (world[(int) (dragon.position.x + dragon.getWidth() / 2)][(int) (dragon.position.y + dragon
					.getHeight() / 2)].getType() == Item.Type.WATER) {
				Vector2 v = new Vector2();

				if (((Water) world[(int) (dragon.position.x + dragon.getWidth() / 2)][(int) (dragon.position.y + dragon
						.getHeight() / 2)]).getDepth() != 9) {
					switch (((Water) world[(int) (dragon.position.x + dragon
							.getWidth() / 2)][(int) (dragon.position.y + dragon
							.getHeight() / 2)]).getRotation()) {
					case 0:
						v.y = waterCurrent;
						break;
					case 90:
						v.x = -1 * waterCurrent;
						break;
					case 180:
						v.y = -1 * waterCurrent;
						break;
					case 270:
						v.x = waterCurrent;
						break;
					}
				}
				dragon.inWater(true, v);
			} else
				dragon.inWater(false, null);

			// Check if player died
			if (player.position.cpy().sub(dragon.position).len() < 0.2f)
				gameState = GameState.DEAD;

			if (showMessage())
				messageTime -= delta;
		}
	}

	public boolean dragonInSight() {
		boolean temp = true;
		if (player.position.cpy().sub(dragon.position).len() < dragonSightDistance) {
			for (Vector2 v = new Vector2(dragon.position); v.cpy()
					.sub(dragon.position).len() < player.position.cpy()
					.sub(dragon.position).len()
					&& temp; v.add(player.position.cpy().sub(dragon.position)
					.limit(1))) {
				if (world[(int) v.x][(int) v.y].getType() == Type.CACTUS)
					temp = false;
			}
		} else
			temp = false;
		return temp;
	}

	private Block selectNew() {
		lookingAt = new Vector2(1 + player.getRad(), 0);
		lookingAt.setAngle(player.getRotation());
		Block temp;
		lookingAt.x += (player.position.x);
		lookingAt.y += (player.position.y);
		// lookingAt.x = (int) lookingAt.x;
		// lookingAt.y = (int) lookingAt.y;
		// Because the renderer doesn't cast to int
		temp = world[(int) lookingAt.x][(int) lookingAt.y];
		if (selectedBlock != temp)
			stopDigging();
		return temp;
	}

	public boolean isDigging() {
		return digging;
	}

	public Vector2 getLookingAt() {
		return lookingAt;
	}

	private boolean WorldBorder() {
		if (lookingAt.x == 127 || lookingAt.x == 0 || lookingAt.y == 127
				|| lookingAt.y == 0)
			return true;
		else
			return false;
	}

	public void startDigging(Type tool) {
		if (!WorldBorder()
				&& (selectedBlock.getTool() == Type.ANYTHING || selectedBlock
						.getTool() == tool))
			digging = true;
	}

	public void stopDigging() {
		digTime = 0;
		digging = false;
	}

	public float getDigPercent() {
		float temp = selectedBlock.getDigTime();
		if (temp == 0)
			return 1;
		else
			return 1 - digTime / temp;
	}

	public Player getPlayer() {
		return player;
	}

	public Dragon getDragon() {
		return dragon;
	}

	public Vector2 getCam() {
		return cam;
	}

	public void setMessage(String message) {
		this.message = message;
		messageTime = 4;
	}

	public String getMessage() {
		return message;
	}

	public boolean showMessage() {
		if (messageTime > 0)
			return true;
		else
			return false;
	}

	public float getMessageTime() {
		return messageTime;
	}

	public int getWorldSize() {
		return worldSize;
	}

	public void setWidth(int width) {
		this.width = width;
		xBounds = width / 4 / zoom;
	}

	public void setHeight(int height) {
		this.height = height;
		yBounds = height / 4 / zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
		xBounds = width / 4 / zoom;
		yBounds = height / 4 / zoom;
	}

	public float getxBounds() {
		return xBounds;
	}

	public float getyBounds() {
		return yBounds;
	}

	public void updateAdjacentBlocks(int x, int y) {
		world[x + 1][y].update(x + 1, y);
		world[x][y + 1].update(x, y + 1);
		world[x - 1][y].update(x - 1, y);
		world[x][y - 1].update(x, y - 1);
	}

	public void pause() {
		if (gameState == GameState.RUNNING)
			gameState = GameState.PAUSED;
	}

	public void resume() {
		if (gameState == GameState.PAUSED)
			gameState = GameState.RUNNING;
	}

	public void reset() {
		player.reset(worldSize / 2, worldSize / 2);
		dragon.reset(random.nextInt(worldSize - 2) + 1,
				random.nextInt(worldSize - 2) + 1);
		cam.set(worldSize / 2, worldSize / 2);
		new Water(this, player);
		// In case we get no water, this is so the game doesn't crash
		// when calling static methods

		// World generation!
		world = new Block[worldSize][worldSize];
		for (int i = 0; i < worldSize; ++i) {
			for (int j = 0; j < worldSize; ++j) {
				if (random.nextInt(cactusChance) == 0)
					if (random.nextInt(smallCactusChance) == 0)
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.SMALL);
					else if (random.nextInt(flowerCactusChance) == 0)
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.FLOWER);
					else
						world[i][j] = new Cactus(this, player,
								Cactus.GrowthState.NORMAL);
				else if (random.nextInt(chestChance) == 0)
					if (random.nextInt(2) == 0)
						world[i][j] = new Chest(this, player, new Shovel(this,
								player));
					else
						world[i][j] = new Chest(this, player, new Bucket(this,
								player));
				else
					world[i][j] = new Sand(this, player);
			}
		}

		// Structure generation!
		for (int i = 0; i < worldSize; ++i) {
			for (int j = 0; j < worldSize; ++j) {
				if (random.nextInt(lakeChance) == 0)
					Lake.makeLake(this, player, i, j, random);
			}
		}

		// Cacti wall
		for (int i = 0; i < worldSize; ++i)
			world[i][0] = new Cactus(this, player, Cactus.GrowthState.NORMAL);
		for (int i = 0; i < worldSize; ++i)
			world[i][worldSize - 1] = new Cactus(this, player,
					Cactus.GrowthState.NORMAL);
		for (int i = 1; i < worldSize - 1; ++i)
			world[0][i] = new Cactus(this, player, Cactus.GrowthState.NORMAL);
		for (int i = 1; i < worldSize - 1; ++i)
			world[worldSize - 1][i] = new Cactus(this, player,
					Cactus.GrowthState.NORMAL);

		while (world[(int) (player.position.x)][(int) (player.position.y + player
				.getRad())].getType() == Type.CACTUS)
			player.position.x += 1;
		selectedBlock = selectNew();

		gameState = GameState.RUNNING;
	}
}
