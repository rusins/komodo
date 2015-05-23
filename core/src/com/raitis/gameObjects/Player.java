package com.raitis.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.raitis.blocks.*;
import com.raitis.gameWorld.GameWorld;
import com.raitis.items.*;
import com.raitis.items.Item.Type;

public class Player {

	private final int hotBarSlots = 5;

	private Vector2 movementVelocity, waterVelocity;
	public Vector2 position;
	private float rotation;
	private float rad;
	private float walkingSpeed = 2;
	private boolean movingUp = false, movingDown = false, movingLeft = false,
			movingRight = false;
	public Item[] hotBar;
	public int[] count;
	private int selected = 0;
	private GameWorld gameWorld;

	public Player(GameWorld gameWorld, int x, int y, float rotation,
			float rad) {
		position = new Vector2(x, y);
		movementVelocity = new Vector2(0, 0);
		waterVelocity = new Vector2(0, 0);
		this.rotation = rotation;
		this.rad = rad;
		hotBar = new Item[hotBarSlots];
		count = new int[hotBarSlots];
		hotBar[0] = new Shovel(gameWorld, this);
		count[0] = 0;
		hotBar[1] = new Bucket(gameWorld, this);
		count[1] = 0;
		hotBar[2] = new WaterBucket(gameWorld, this);
		count[2] = 0;
		hotBar[3] = new Cactus(gameWorld, this, Cactus.GrowthState.NORMAL);
		count[3] = 0;
		hotBar[4] = new Sand(gameWorld, this);
		count[4] = 0;
		this.gameWorld = gameWorld;
	}

	public void update(float delta) {
		position.add(movementVelocity.cpy().add(waterVelocity).scl(delta));
	}

	public void updateMovement(Vector2 v) {
		movementVelocity = v.scl(10);
		// 10, so you CAN walk slow if you want to,
		// but you will still have full walking speed
		movementVelocity.limit(walkingSpeed);
		if (movementVelocity.x != 0 || movementVelocity.y != 0)
			rotation = movementVelocity.angle();
	}

	private void updateMovement() {
		if (movingUp)
			movementVelocity.y = -1 * walkingSpeed;
		else if (movingDown)
			movementVelocity.y = walkingSpeed;
		else
			movementVelocity.y = 0;
		if (movingRight)
			movementVelocity.x = walkingSpeed;
		else if (movingLeft)
			movementVelocity.x = -1 * walkingSpeed;
		else
			movementVelocity.x = 0;
		movementVelocity.limit(walkingSpeed);
		if (movingUp || movingDown || movingLeft || movingRight)
			rotation = movementVelocity.angle();
	}

	public void inWater(boolean in, Vector2 v) {
		if (in) {
			walkingSpeed = 1;
			waterVelocity = v;
		} else {
			walkingSpeed = 2;
			waterVelocity.x = 0;
			waterVelocity.y = 0;
		}
		movementVelocity.scl(2).limit(walkingSpeed);
	}

	public float getRotation() {
		return rotation;
	}

	public float getRad() {
		return rad;
	}
	
	public float getDiam() {
		return 2 * rad;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
		if (movingUp)
			movingDown = false;
		updateMovement();
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
		if (movingDown)
			movingUp = false;
		updateMovement();
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
		if (movingLeft)
			movingRight = false;
		updateMovement();
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
		if (movingRight)
			movingLeft = false;
		updateMovement();
	}

	public Vector2 getVelocity() {
		return movementVelocity.cpy().add(waterVelocity);
	}

	public Item getItem(int slot) {
		return hotBar[slot];
	}

	public void giveItem(Type item) {
		switch (item) {
		case SHOVEL:
			if (count[0] == 0) {
				gameWorld.setMessage("You got a shovel!");
			} else
				gameWorld
						.setMessage("You found another shovel, good for you! :P");
			count[0]++;
			break;
		case BUCKET:
			count[1]++;
			gameWorld.setMessage("You got a bucket!");
			break;
		case WATER_BUCKET:
			count[2]++;
			break;
		case CACTUS:
			count[3]++;
			break;
		case SAND:
			count[4]++;
			break;
		default:
			gameWorld
					.setMessage("ERROR - Attempted to pick up unintended item - "
							+ item);
		}
	}

	public void useItem(Type item) {
		switch (item) {
		case SHOVEL:
			count[0]--;
			break;
		case BUCKET:
			count[1]--;
			break;
		case WATER_BUCKET:
			count[2]--;
			break;
		case CACTUS:
			count[3]--;
			break;
		case SAND:
			count[4]--;
			break;
		default:
			gameWorld
					.setMessage("ERROR - Attempted to take away unintended item - "
							+ item);
		}
	}

	public int getHotBarSlots() {
		return hotBarSlots;
	}

	public void setSelected(int s) {
		selected = s;
	}

	public void incSelected(int amount) {
		selected += amount;
		if (selected >= hotBarSlots)
			selected -= hotBarSlots;
		else if (selected < 0)
			selected += hotBarSlots;
	}

	public int getSelected() {
		return selected;
	}

	public Item getSelectedItem() {
		return hotBar[selected];
	}

	public void startUsing() {
		if (hotBar[selected] != null && count[selected] != 0)
			hotBar[selected].startUsing((int) gameWorld.getLookingAt().x,
					(int) gameWorld.getLookingAt().y);
		else
			gameWorld.startDigging(Type.ANYTHING);
	}

	public void stopUsing() {
		if (hotBar[selected] != null)
			hotBar[selected].stopUsing();
		else
			gameWorld.stopDigging();
	}

	public void reset(int x, int y) {
		position.set(x, y);
		movementVelocity.set(0, 0);
		rotation = 0;
		waterVelocity.set(0, 0);
		for (int i = 0; i < hotBarSlots; ++i)
			count[i] = 0;
	}

}
