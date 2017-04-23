package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Rockets",
		width = 3,
		height = 3
)
public class RocketModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.H2O, 100.f);
		buildCost.put(Resource.Fe, 600.f);
		buildCost.put(Resource.Si, 200.f);
		ModuleFactory.putBuildCost(RocketModule.class, buildCost);
	}

	public RocketModule(ModuleLocation moduleLocation) {
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
}
