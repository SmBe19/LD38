package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

public class CrossModule extends Module {

	public CrossModule(ModuleLocation moduleLocation) {
		super(moduleLocation);
	}

	@Override
	protected float getBuildSpeedMultiplier() {
		return 5;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleCross.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullCross.texture();
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 5);
	}
}
