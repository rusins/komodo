package com.raitis.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.Item.Type;

public class Dragon {

	private final int smartness = 30;

	private float waitingTime = 1;
	private float walkingSpeed = 2.5f;
	public Vector2 position;
	private Vector2 velocity;
	private GameWorld gameWorld;
	private Player player;
	private boolean inWater = false;
	private boolean playerVisible = false;
	private float width, height;
	private Vector2[] path;
	private int currentPosition = 0;
	private boolean[][] beenHere;
	private boolean followingPath = false;

	public boolean isPlayerVisible() {
		return playerVisible;
	}

	public Dragon(GameWorld gameWorld, Player player, int x, int y, float width,
			float height) {
		this.gameWorld = gameWorld;
		this.player = player;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
		this.width = width;
		this.height = height;
		path = new Vector2[smartness];
		beenHere = new boolean[smartness * 2 + 1][smartness * 2 + 1];
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void update(float delta) {
		if (playerVisible) {
			if (!inWater) {
				boolean temp = true;
				for (Vector2 v = new Vector2(position); v.cpy().sub(position)
						.len() < player.position.cpy().sub(position).len()
						&& temp; v.add(player.position.cpy().sub(position)
						.limit(1))) {
					if (gameWorld.world[(int) v.x][(int) v.y].getType() == Type.CACTUS)
						temp = false;
					else if (gameWorld.world[(int) (v.x + width)][(int) v.y].getType() == Type.CACTUS)
						temp = false;
					else if (gameWorld.world[(int) (v.x + width)][(int)(v.y + height)].getType() == Type.CACTUS)
						temp = false;
					else if (gameWorld.world[(int) v.x][(int) (v.y + height)].getType() == Type.CACTUS)
						temp = false;
				}
				if (temp) {
					followingPath = false;
					velocity = player.position.cpy().sub(position);
					velocity.scl(50).limit(walkingSpeed);
				} else {
					if (!followingPath)
						updatePath();
					// TODO
					// cehck if we've stepped up in the path
					// go to the next thing in the path
				}
			}
			position.add(velocity.cpy().scl(delta));
		}
	}

	public void inWater(boolean in, Vector2 v) {
		if (in) {
			if (v.len() == 0) {
				walkingSpeed = 1.5f;
				inWater = false;
			} else {
				velocity = v;
				inWater = true;
			}
		} else {
			inWater = false;
			walkingSpeed = 2.5f;
		}
	}

	public void setPlayerVisible(float delta) {
		waitingTime -= delta;
		if (waitingTime <= 0)
			playerVisible = true;
	}

	public void updatePath() {
		
	}

	public void reset(int x, int y) {
		position.set(x, y);
		velocity.set(0, 0);
		playerVisible = false;
		followingPath = false;
	}

	/*private boolean recursion(int x, int y) {
		if (gameWorld.world[x][y].getType() == Type.CACTUS)
			return false;
		beenHere[x][y] = true;
	}*/
}
