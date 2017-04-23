package com.smeanox.games.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.io.IOAnimation;

import java.util.Map;

public class Dude {

	private IOAnimation texture;
	private float x, y, hp;
	private boolean hadEnoughResources;

	public Dude(IOAnimation texture) {
		this.texture = texture;
		this.x = 0;
		this.y = 0;
		this.hp = 1;
		hadEnoughResources = true;
	}

	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta){
		hadEnoughResources = true;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public boolean isHadEnoughResources() {
		return hadEnoughResources;
	}

	public void update(float delta) {

	}

	public void draw(SpriteBatch batch, float time) {
		batch.draw(texture.keyFrame(time), x, y, 1, 1);
	}
}
