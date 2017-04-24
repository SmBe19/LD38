package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.SpaceStation;

public class RadioModule extends Module {

	public RadioModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.RIGHT] = false;
		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
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
			uiElements.add(new Button(5, 5, width - 10, 10, "Scan for signals", new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					label.color = isWorking() ? Color.BLACK : Color.FIREBRICK;
				}
			}, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					if (isWorking()) {
						if (SpaceStation.get().isWorldWarStarted()) {
							SpaceStation.get().getEnabledModuleTypes().add(ModuleType.CryogenicModule);
							SpaceStation.get().getStoryManager().highlighted = ModuleType.CryogenicModule;
							SpaceStation.get().addMessage(SpaceStation.get().getMessageManager().noSurvivors());
						} else {
							SpaceStation.get().addMessage("You receive many signals.\nThere doesn't seem to be anything interesting.");
						}
					}
				}
			}));
		}
	}
}
