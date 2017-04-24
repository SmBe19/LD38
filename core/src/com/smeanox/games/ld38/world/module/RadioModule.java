package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

import java.util.HashMap;
import java.util.Map;

public class RadioModule extends Module {

	public RadioModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.RIGHT] = false;
		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	protected float getBuildSpeedMultiplier() {
		return 0.25f;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleRadio.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return null;
	}

	@Override
	public Window createWindow(float x, float y) {
		return new RadioWindow(x, y);
	}

	private class RadioWindow extends Window {

		public RadioWindow(float x, float y) {
			super(x, y, 200, 20);
		}

		@Override
		public void init() {
			if(SpaceStation.get().isWorldWarStarted()) {
				uiElements.add(new Button(5, 5, width - 10, 10, "Scan for signals", null, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						SpaceStation.get().addMessage("You don't receive any signals.\nThere are no survivors on earth");
					}
				}));
			} else {
				uiElements.add(new Button(5, 5, width - 10, 10, "Scan for humans", null, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
					}
				}));
			}
		}
	}
}
