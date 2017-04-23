package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Command Module",
		width = 3,
		height = 3
)
public class MainModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Electricity, 5.f);
		ModuleFactory.putBuildCost(MainModule.class, buildCost);
	}

	public MainModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
	}

	@Override
	public boolean canAttachSolarPanel() {
		return false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleMain.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullMain.texture();
	}

	@Override
	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add) {
		resources.get(Resource.Electricity).value += (add ? 1 : -1) * 1000;
		resources.get(Resource.Fe).value += (add ? 1 : -1) * 1000;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, Resource.Electricity, -delta * 100) &&
				tryUseResource(resources, Resource.Fe, -delta * 100);
	}
}
