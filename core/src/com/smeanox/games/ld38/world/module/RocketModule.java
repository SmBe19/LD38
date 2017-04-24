package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.task.RocketTask;

import java.util.Map;

public class RocketModule extends Module {

	private float rocketStart, rocketArrive;

	public RocketModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.RIGHT] = false;
		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;

		rocketStart = -Consts.SMALLROCKET_ANIMATION_DURATION;
	}

	public void setRocketFlight(float rocketStart, float rocketArrive){
		System.out.println("Rocket flight " + rocketStart + " " + rocketArrive + " " + SpaceStation.get().getTime());
		this.rocketStart = rocketStart;
		this.rocketArrive = rocketArrive;
	}

	@Override
	public boolean canRandomWalk() {
		return false;
	}

	@Override
	protected float getBuildSpeedMultiplier() {
		return 0.25f;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleRockets.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return null;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 5);
	}

	private void drawRocket(SpriteBatch batch, float time) {
		if(finished) {
			float gameTime = SpaceStation.get().getTime();
			TextureRegion texture = IOAnimation.SmallRocket.keyFrame(time);
			float interp = 1, fade = 1, scale = 1, offset = 0, progress = 0;
			if(rocketStart <= gameTime && gameTime - rocketStart < Consts.SMALLROCKET_ANIMATION_DURATION){
				progress = (gameTime - rocketStart) / Consts.SMALLROCKET_ANIMATION_DURATION;
			} else if (gameTime <= rocketArrive && rocketArrive - gameTime < Consts.SMALLROCKET_ANIMATION_DURATION) {
				progress = (rocketArrive - gameTime) / Consts.SMALLROCKET_ANIMATION_DURATION;
			} else if (rocketStart < gameTime && gameTime < rocketArrive){
				return;
			}
			interp = MathUtils.cos(MathUtils.PI * progress) / 2 + .5f;
			offset = (1 - interp) * Consts.SMALLROCKET_ANIMATION_OFFSET;
			fade = MathUtils.clamp(interp * Consts.SMALLROCKET_ANIMATION_FADESPEED, 0, 1);

			float width, height, x, y;
			width = texture.getRegionWidth() * scale / Consts.SPRITE_SIZE;
			height = texture.getRegionHeight() * scale / Consts.SPRITE_SIZE;
			x = getModuleLocation().getRotX() + 1;
			y = getModuleLocation().getRotY() + 1;

			switch (getModuleLocation().getRotation()){
				case Consts.RIGHT:
					x += offset;
					break;
				case Consts.UP:
					y += offset;
					break;
				case Consts.LEFT:
					x -= offset;
					break;
				case Consts.DOWN:
					y -= offset;
					break;
			}

			Color oldColor = batch.getColor().cpy();
			batch.setColor(1, 1, 1, fade);
			batch.draw(texture, x, y, 0.5f, 0.5f, width, height, 1, 1, getModuleLocation().getRotation() * 90);
			batch.setColor(oldColor);
		}
	}

	@Override
	public void drawBackground(SpriteBatch batch, float time) {
		drawRocket(batch, time);
		super.drawBackground(batch, time);
	}

	@Override
	public Window createWindow(float x, float y) {
		return new RocketWindow(x, y);
	}

	private class RocketWindow extends Window {

		public RocketWindow(float x, float y) {
			super(x, y, 200, 20);
		}

		protected boolean haveEnoughResources(){
			if(!isWorking() || SpaceStation.get().getTime() < rocketArrive){
				return false;
			}
			Map<Resource, GenericRapper<Float>> resources = SpaceStation.get().getResources();
			Map<Resource, Float> cost = RocketTask.getCost();
			for (Resource resource : Resource.values()) {
				if (cost.containsKey(resource)) {
					if (resources.get(resource).value < cost.get(resource)) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public void init() {
			uiElements.add(new Button(5, 5, width - 10, 10, "Search asteroid", new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					label.color = haveEnoughResources() ? Color.WHITE : Color.RED;
				}
			}, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					if (haveEnoughResources()) {
						RocketTask rocketTask = new RocketTask(RocketModule.this);
						rocketTask.removeResources();
						rocketStart = Float.POSITIVE_INFINITY;
						rocketArrive = Float.POSITIVE_INFINITY;
						SpaceStation.get().addTask(rocketTask);
					}
				}
			}));
		}
	}
}
