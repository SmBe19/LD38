package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

import java.util.HashMap;
import java.util.Map;

public class SolarModule extends Module {

	public SolarModule(ModuleLocation moduleLocation) {
		super(moduleLocation);
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.SolarPanel.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return null;
	}

	@Override
	public boolean canAttachSolarPanel() {
		return false;
	}

	@Override
	public boolean isWorking() {
		return super.isWorking() && !SpaceStation.get().isEclipse()
				&& SpaceStation.get().getTimeOfDay() < (Consts.DURATION_DAY - Consts.DURATION_NIGHT);
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, -180);
	}
}
