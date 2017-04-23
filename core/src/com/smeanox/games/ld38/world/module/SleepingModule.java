package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Sleep Module",
		width = 2,
		height = 3
)
public class SleepingModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Fe, 200.f);
		ModuleFactory.putBuildCost(SleepingModule.class, buildCost);
	}

	public SleepingModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleSleeping.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullWide23.texture();
	}

	@Override
	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add) {
		resources.get(Resource.Humans).value += (add ? 1 : -1) * 2;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 60);
	}
}
