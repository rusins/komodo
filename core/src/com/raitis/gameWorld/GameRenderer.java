package com.raitis.gameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.raitis.blocks.Water;
import com.raitis.gameObjects.Dragon;
import com.raitis.gameObjects.Joystick;
import com.raitis.gameObjects.Player;
import com.raitis.gameWorld.GameWorld.GameState;
import com.raitis.items.Item.Type;
import com.raitis.komodoHelpers.AssetLoader;

public class GameRenderer {

	private GameWorld gameWorld;
	private Player player;
	private Dragon dragon;
	private Joystick joystick;
	private OrthographicCamera camera;
	private Vector2 cam;
	private int midX, midY, height, width;
	private int zoom;
	private float rotation = 0;
	private final int rotationSpeed = 360;
	// For the spinning joystick circle
	private boolean debugMode = false;
	private int fps;
	private float fpsRunTime = 0;

	private SpriteBatch batcher;
	private ShapeRenderer shapeRenderer;

	public GameRenderer(GameWorld gameWorld, int width, int height, int MidX,
			int MidY, int zoom, Joystick joystick) {
		AssetLoader.load();
		this.gameWorld = gameWorld;
		player = gameWorld.getPlayer();
		dragon = gameWorld.getDragon();
		cam = gameWorld.getCam();
		this.midX = MidX;
		this.midY = MidY;
		this.height = height;
		this.width = width;
		this.zoom = zoom;
		this.joystick = joystick;
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height); // Do we want? (yes), width,
												// height

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(camera.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

	}

	public void toggleDebug() {
		debugMode = !debugMode;
	}

	public void render(float delta, float runTime) {
		// I WANT delta. For calculating rotation speed for ex.
		// runTime is used for animations
		rotation += rotationSpeed * delta;
		fpsRunTime += delta;

		batcher.begin();
		// Disable transparency
		// This is good for performance when drawing images that do not require
		// transparency.
		// batcher.disableBlending();

		// Drawing the blocks and (entities?)
		int tempxMin, tempxMax, tempyMin, tempyMax, tempx, tempy;
		tempx = width / zoom / 2;
		tempxMin = (int) cam.x - tempx - 2;
		if (tempxMin < 0)
			tempxMin = 0;
		tempxMax = (int) cam.x + tempx + 2;
		if (tempxMax > gameWorld.getWorldSize())
			tempxMax = gameWorld.getWorldSize();
		tempy = height / zoom / 2;
		tempyMin = (int) cam.y - tempy - 2;
		if (tempyMin < 0)
			tempyMin = 0;
		tempyMax = (int) cam.y + tempy + 2;
		if (tempyMax > gameWorld.getWorldSize())
			tempyMax = gameWorld.getWorldSize();
		for (int i = tempxMin; i < tempxMax; ++i) {
			for (int j = tempyMin; j < tempyMax; ++j)
				if (gameWorld.world[i][j].getType() != Type.WATER) {
					batcher.draw(gameWorld.world[i][j].getTexture(),
							(i - cam.x) * zoom + midX, (j - cam.y) * zoom
									+ midY, zoom, zoom);
				} else {
					batcher.draw(gameWorld.world[i][j].getTexture(runTime),
							(i - cam.x) * zoom + midX, (j - cam.y) * zoom
									+ midY, zoom / 2, zoom / 2, zoom, zoom, 1,
							1, ((Water) gameWorld.world[i][j]).getRotation());
					if (debugMode) {
						AssetLoader.font.setColor(0, 0, 0, 1);
						AssetLoader.font.draw(batcher, Integer
								.toString(((Water) gameWorld.world[i][j])
										.getDepth()), (i - cam.x) * zoom + midX
								+ zoom / 2, (j - cam.y) * zoom + midY + zoom
								/ 2);

					}
				}
		}

		batcher.enableBlending();

		// Drawing Selected Block
		batcher.draw(AssetLoader.selected,
				((int) gameWorld.getLookingAt().x - cam.x) * zoom + midX,
				((int) gameWorld.getLookingAt().y - cam.y) * zoom + midY,
				zoom / 2, zoom / 2, zoom, zoom, gameWorld.getDigPercent(),
				gameWorld.getDigPercent(), 0);

		// Drawing Player
		batcher.draw(AssetLoader.playerTextureRegion, (player.position.x
				- player.getRad() - cam.x)
				* zoom + midX,
				(player.position.y - player.getRad() - cam.y) * zoom
						+ midY, player.getRad() * zoom,
				player.getRad() * zoom, player.getDiam() * zoom,
				player.getDiam() * zoom, 1, 1, player.getRotation());

		// Drawing Dragon
		batcher.draw(AssetLoader.circleTextureRegion,
				(dragon.position.x - cam.x) * zoom + midX,
				(dragon.position.y - cam.y) * zoom + midY, 32, 32, 64, 64, 1,
				1, 0);

		// Drawing Message
		if (gameWorld.showMessage()) {
			if (gameWorld.getMessageTime() > 1)
				AssetLoader.font.setColor(0, 0, 0, 1);
			else
				AssetLoader.font.setColor(0, 0, 0, gameWorld.getMessageTime());
			AssetLoader.font.draw(batcher, gameWorld.getMessage(), 50, 50);
		}

		// Drawing Joystick

		if (Gdx.app.getType() == ApplicationType.Android) {
			batcher.draw(joystick.texture, joystick.getX(), joystick.getY(),
					joystick.getMiddle(), joystick.getMiddle(),
					joystick.getSize(), joystick.getSize(), 1, 1, rotation);
			batcher.draw(AssetLoader.blueButtonTextureRegion,
					joystick.getOriginX() - 16, joystick.getOriginY() - 16, 16,
					16, 32, 32, 1, 1, 0);
			batcher.draw(AssetLoader.whiteCircleTextureRegion,
					joystick.getOriginX() - joystick.getMiddle() * 4,
					joystick.getOriginY() - joystick.getMiddle() * 4,
					joystick.getMiddle() * 8, joystick.getMiddle() * 8);
		}

		// Drawing HotBar
		for (int i = 0; i < player.getHotBarSlots(); ++i) {
			if (player.count[i] > 0)
				batcher.draw(player.hotBar[i].getTexture(), (i - 5) * zoom
						+ width + zoom / 8, height - zoom * 7 / 8,
						zoom * 3 / 4, zoom * 3 / 4);
			if (i == player.getSelected())
				batcher.draw(AssetLoader.selected, (i - 5) * zoom + width,
						height - zoom, zoom, zoom);
			else
				batcher.draw(AssetLoader.slot, (i - 5) * zoom + width, height
						- zoom, zoom, zoom);
			AssetLoader.font.setColor(0, 0, 0, 1);
		}
		for (int i = 0; i < player.getHotBarSlots(); ++i) {
			if (player.count[i] > 0)
				AssetLoader.font.draw(batcher, "x " + player.count[i], (i - 4)
						* zoom + width - zoom / 2, height - zoom / 3);
		}

		// Debug info
		if (debugMode) {
			AssetLoader.font.setColor(0, 0, 0, 1);
			AssetLoader.font.draw(batcher, "FPS: " + fps, width - 100, 10);
			if (fpsRunTime > 1) {
				fps = (int) (1 / delta);
				fpsRunTime--;
			}
			AssetLoader.font.draw(batcher, "Zoom: " + zoom, width - 100, 30);
			AssetLoader.font.setColor(1, 0, 0, 1);
			AssetLoader.font.draw(batcher, "X: " + (int) player.position.x
					+ " Y: " + (int) player.position.y, width - 100, 50);
			AssetLoader.font.setColor(0, 0, 1, 1);
			AssetLoader.font.draw(batcher, "X: " + (int) dragon.position.x
					+ " Y: " + (int) dragon.position.y, width - 100, 70);
			if (gameWorld.dragonInSight()) {
				AssetLoader.font.setColor(0, 1, 0, 1);
				AssetLoader.font.draw(batcher, "Dragon sees the player",
						width - 175, 90);
			} else {
				AssetLoader.font.setColor(1, 0, 0, 1);
				AssetLoader.font.draw(batcher, "Dragon doesn't see the player",
						width - 200, 90);
			}
		}

		if (gameWorld.gameState == GameState.DEAD) {
			batcher.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 0, 0.5f);
			shapeRenderer.rect(0, 0, width, height);
			shapeRenderer.setColor(0, 0, 1, 0.5f);
			shapeRenderer.rect(width * 3 / 4, height * 2 / 3, width / 6,
					height / 8);
			shapeRenderer.rect(midX / 5, midY, width / 3, height / 3);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			batcher.begin();
			AssetLoader.gameOverFont.draw(batcher, "GAME OVER", width / 4,
					height / 3);
			AssetLoader.font.setColor(1, 1, 1, 1);
			AssetLoader.font.draw(batcher, "Respawn", width * 3 / 4 + 20,
					height * 2 / 3 + 20);
			AssetLoader.font.draw(batcher, "CREDITS", midX * 0.4f, midY + 5);
			AssetLoader.font.draw(batcher, "Programming - Raitis Krikis",
					midX * 0.25f, midY * 1.2f);
			AssetLoader.font.draw(batcher, "Textures - Oskars Cizevskis",
					midX * 0.25f, midY * 1.3f);
		} else if (gameWorld.gameState == GameState.INTRO) {
			batcher.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 1, 0.5f);
			shapeRenderer.rect(0,
					joystick.getOriginY() - 5 * joystick.getMiddle(),
					joystick.getOriginX() + 5 * joystick.getMiddle(), height);
			shapeRenderer.rect(midX, 0, width, height);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			batcher.begin();
			AssetLoader.font.setColor(1, 1, 1, 1);
			AssetLoader.font.draw(batcher, "Use the joystick to move",
					width / 32, height / 3 * 2);
			AssetLoader.font.draw(batcher, "Press here to use", width / 4 * 3,
					midY);
			AssetLoader.font.draw(batcher, "items and/or dig", width / 4 * 3,
					midY + 30);
		}

		// End the SpriteBatch
		batcher.end();
	}

	public void setWidth(int width) {
		this.width = width;
		camera.setToOrtho(true, width, height);
		batcher.setProjectionMatrix(camera.combined);
	}

	public void setHeight(int height) {
		this.height = height;
		camera.setToOrtho(true, width, height);
		batcher.setProjectionMatrix(camera.combined);
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
}