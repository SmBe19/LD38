package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Storage",
		width = 2,
		height = 3
)
public class StorageModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Fe, 150.f);
		ModuleFactory.putBuildCost(StorageModule.class, buildCost);
	}

	public StorageModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleStorage.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullWide3.texture();
	}

	@Override
	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add) {
		resources.get(Resource.H2).value += (add ? 1 : -1) * 40;
		resources.get(Resource.O2).value += (add ? 1 : -1) * 360;
		resources.get(Resource.H2O).value += (add ? 1 : -1) * 360;
		resources.get(Resource.Food).value += (add ? 1 : -1) * 180;
		resources.get(Resource.Electricity).value += (add ? 1 : -1) * 360;
		resources.get(Resource.Fe).value += (add ? 1 : -1) * 600;
		resources.get(Resource.Si).value += (add ? 1 : -1) * 200;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 45);
	}
}
