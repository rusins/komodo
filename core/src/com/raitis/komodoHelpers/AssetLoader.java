package com.raitis.komodoHelpers;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AssetLoader {
	private static Texture playerTexture, circleTexture, doubleCircleTexture,
			blueButtonTexture, whiteCircleTexture, blocks, items, hotBar,
			waterTexture;
	public static Sprite playerTextureRegion, circleTextureRegion,
			doubleCircleTextureRegion, blueButtonTextureRegion,
			whiteCircleTextureRegion, sand, smallCactus, flowerCactus, cactus,
			chest, dirt, shovel, bucket, waterBucket, slot, selected;
	public static TextureRegion[] water;
	public static Animation waterAnimation;
	public static BitmapFont font, gameOverFont;

	public static void load() {
		font = new BitmapFont(true);
		if (Gdx.app.getType() == ApplicationType.Android)
			font.scale(2);
		gameOverFont = new BitmapFont(true);
		gameOverFont.scale(3);
		gameOverFont.setColor(1, 1, 1, 1);
		playerTexture = new Texture(Gdx.files.internal("data/chad.png"));
		circleTexture = new Texture(Gdx.files.internal("data/circle.png"));
		doubleCircleTexture = new Texture(
				Gdx.files.internal("data/doublecircle.png"));
		blueButtonTexture = new Texture(
				Gdx.files.internal("data/bluebutton.png"));
		whiteCircleTexture = new Texture(
				Gdx.files.internal("data/whitecircle.png"));
		blocks = new Texture(Gdx.files.internal("data/blocks.png"));
		items = new Texture(Gdx.files.internal("data/items.png"));
		hotBar = new Texture(Gdx.files.internal("data/hotbar.png"));
		waterTexture = new Texture(Gdx.files.internal("data/water.png"));
		// playerTexture.setFilter(TextureFilter.Nearest,
		// TextureFilter.Nearest);
		// Not needed, as Nearest is the default :P
		playerTextureRegion = new Sprite(playerTexture, 0, 0, 88, 88);
		playerTextureRegion.flip(false, true);
		circleTextureRegion = new Sprite(circleTexture, 0, 0, 512, 512);
		// circleTextureRegion.flip(false, true);
		doubleCircleTextureRegion = new Sprite(doubleCircleTexture, 0, 0, 512,
				512);
		blueButtonTextureRegion = new Sprite(blueButtonTexture, 0, 0, 320, 320);
		whiteCircleTextureRegion = new Sprite(whiteCircleTexture, 0, 0, 300,
				300);
		sand = new Sprite(blocks, 0, 0, 256, 256);
		smallCactus = new Sprite(blocks, 256, 0, 256, 256);
		cactus = new Sprite(blocks, 0, 256, 256, 256);
		flowerCactus = new Sprite(blocks, 256, 256, 256, 256);
		chest = new Sprite(blocks, 256, 512, 256, 256);
		chest.flip(false, true);
		dirt = new Sprite(blocks, 0, 512, 256, 256);
		shovel = new Sprite(items, 256, 0, 256, 256);
		shovel.flip(false, true);
		bucket = new Sprite(items, 0, 256, 256, 256);
		bucket.flip(false, true);
		waterBucket = new Sprite(items, 256, 256, 256, 256);
		waterBucket.flip(false, true);
		slot = new Sprite(hotBar, 0, 0, 16, 16);
		selected = new Sprite(hotBar, 16, 0, 16, 16);
		water = new TextureRegion[8];
		for (int i = 0; i < 8; ++i)
			water[i] = new TextureRegion(waterTexture, i * 256, 0, 256, 256);
		waterAnimation = new Animation(0.125f, water);
	}

	public static void dispose() {
		playerTexture.dispose();
		circleTexture.dispose();
		doubleCircleTexture.dispose();
		blocks.dispose();
		items.dispose();
	}
}
